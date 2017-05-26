package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.game.infra.Time;
import br.ol.eightball.math.Vec2;
import br.ol.eightball.math.Vec4;
import br.ol.eightball.renderer3d.core.Renderer;
import br.ol.eightball.renderer3d.parser.wavefront.Obj;
import br.ol.eightball.renderer3d.parser.wavefront.WavefrontParser;
import br.ol.eightball.renderer3d.parser.wavefront.WavefrontParser.Face;
import br.ol.eightball.renderer3d.shader.GouraudShaderWithTexture;
import java.util.ArrayList;
import java.util.List;

/**
 * OLPresents class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OLPresents extends EightBallEntity {
    
    private final List<FaceEffect> facesEffect = new ArrayList<FaceEffect>();
    private double p;
    
    public OLPresents(String name, EightBallScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/ol_presents.obj", 50);
        extractFacesEffect();
        setVisible(true);
    }

    private void extractFacesEffect() {
        for (Obj obj : mesh) {
            for (WavefrontParser.Face face : obj.faces) {
                FaceEffect faceEffect = new FaceEffect(face);
                facesEffect.add(faceEffect);
            }
        }        
    }
    
    @Override
    public void updateOlPresents(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    transform.setIdentity();
                    transform.translate(0, 40, 0);
                    
                    scene.broadcastMessage("fadeIn");
                    instructionPointer = 1;
                case 1:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;
                case 2:
                    while (System.currentTimeMillis() - waitTime < 50) {
                        break yield;
                    }
                    instructionPointer = 3;
                case 3:
                    p = p + Time.delta * 0.0000000005;
                    if (p > 2) {
                        p = 2;
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 4;
                    }
                    break yield;
                case 4:
                    while (System.currentTimeMillis() - waitTime < 4000) {
                        break yield;
                    }
                    instructionPointer = 5;
                case 5:
                    // reserved
                    instructionPointer = 6;
                    break yield;
                case 6:
                    scene.broadcastMessage("fadeOut");
                    instructionPointer = 7;
                case 7:
                    fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    //scene.getEngine().renderer.getColorBuffer().setBackgroundColor(Color.BLACK);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 8;
                case 8:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    scene.setState(State.TITLE);
                    break yield;
            }
        }
    }

    @Override
    public void preDraw(Renderer renderer) {
        renderer.setBackfaceCullingEnabled(true);
        if (!visible || mesh == null) {
            GouraudShaderWithTexture.minIntensity = 0.5;
            return;
        }
        super.preDraw(renderer); 
        renderer.setBackfaceCullingEnabled(false);
        GouraudShaderWithTexture.minIntensity = 1;
    }
    
    @Override
    public void draw(Renderer renderer) {
        if (!visible || mesh == null) {
            GouraudShaderWithTexture.minIntensity = 0.5;
            return;
        }
        renderer.setMaterial(mesh.get(0).material);
        renderer.begin();
        for (FaceEffect face : facesEffect) {
            for (int f=0; f<3; f++) {
                Vec4 v = face.getVertex(f, p);
                Vec4 n = face.normal[f];
                Vec2 t = face.texture[f];
                renderer.setTextureCoordinates(t.x, t.y);
                renderer.setNormal(n.x, n.y, n.z);
                renderer.setVertex(v.x, v.y, v.z);
            }
        }
        renderer.end();            
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state == State.OL_PRESENTS;
        if (visible) {
            //scene.getEngine().renderer.getColorBuffer().setBackgroundColor(Color.WHITE);
            instructionPointer = 0;
        }
    }
    
    // faces (flying) effect 
    
    private class FaceEffect extends Face {
        
        final Vec4[] originalVertex = new Vec4[3];
        final Vec4 transformedVertex = new Vec4();
        final Vec4 currentTranslation = new Vec4();
        final Vec4 currentRotation = new Vec4();
        final Vec4 translationSrc = new Vec4();
        final Vec4 translationDest = new Vec4();
        final Vec4 rotationSrc = new Vec4();
        final Vec4 rotationDest = new Vec4();
        final double delay = 1 * Math.random();
        
        public FaceEffect(Face originalFace) {
            super(originalFace.vertex, originalFace.normal, originalFace.texture);
            originalVertex[0] = new Vec4(originalFace.vertex[0]);
            originalVertex[1] = new Vec4(originalFace.vertex[1]);
            originalVertex[2] = new Vec4(originalFace.vertex[2]);
            translationDest.set(originalVertex[0]);
            translationDest.add(originalVertex[1]);
            translationDest.add(originalVertex[2]);
            translationDest.multiply(1 / 3.0);
            translationSrc.set(translationDest);
            translationSrc.z += 700 + 500 * Math.random();
            originalVertex[0].sub(translationDest);
            originalVertex[1].sub(translationDest);
            originalVertex[2].sub(translationDest);
            rotationSrc.set((2 * Math.PI) + (2 * Math.PI) * Math.random()
                    , (2 * Math.PI) + (2 * Math.PI) * Math.random()
                    , (2 * Math.PI) + (2 * Math.PI) * Math.random(), 0);
            rotationDest.set(0, 0, 0, 0);
        }
        
        public Vec4 getVertex(int i, double p) {
            p = p - delay;
            p = p < 0 ? 0 : p > 1 ? 1 : p;
            transformedVertex.set(originalVertex[i]);
            Vec4.lerp(rotationSrc, rotationDest, currentRotation, p);
            transformedVertex.rotateX(currentRotation.x);
            transformedVertex.rotateY(currentRotation.y);
            transformedVertex.rotateZ(currentRotation.z);
            Vec4.lerp(translationSrc, translationDest, currentTranslation, p);
            transformedVertex.translate(currentTranslation);
            return transformedVertex;
        }
        
    }
    
}
