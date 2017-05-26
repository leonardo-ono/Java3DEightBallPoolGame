package br.ol.eightball.renderer3d.shader;

import br.ol.eightball.math.MathUtil;
import br.ol.eightball.renderer3d.core.Image;
import br.ol.eightball.renderer3d.core.Material;
import br.ol.eightball.renderer3d.core.Renderer;
import br.ol.eightball.renderer3d.core.Shader;
import br.ol.eightball.renderer3d.rasterizer.Vertex;

/**
 *
 * @author leonardo
 */
public class BallShadowShader extends Shader {
    
    private final int[] destColor = new int[4];
    
    public BallShadowShader() {
        super(0, 0, 3);
    }
    
    @Override
    public void processVertex(Renderer renderer, Vertex vertex) {
        // renderer.doVertexMVPTransformation(vertex);
        
        // perspective correct texture mapping
        double zInv = 1 / vertex.p.z;
        vertex.vars[0] = zInv;
        vertex.vars[1] = vertex.st.x * zInv;
        vertex.vars[2] = vertex.st.y * zInv;
    }
    
    @Override
    public void processPixel(Renderer renderer, int xMin, int xMax, int x, int y, double[] vars) {
        double depth = vars[0];
        double z = 1 / depth;
        double s = vars[1] * z;
        double t = vars[2] * z;

        s = s > 1 ? s - (int) s : s < 0 ? (int) s - s : MathUtil.clamp(s, 0, 1);
        t = t > 1 ? t - (int) t : t < 0 ? (int) t - t : MathUtil.clamp(t, 0, 1);

        Material material = renderer.getMaterial();
        Image texture = null;
        
        if (material != null) {
            texture = renderer.getMaterial().map_kd;
        }
        
        if (texture != null) {
            int tx = (int) (s * (texture.getWidth() - 1));
            int textureHeight = texture.getHeight() - 1;
            int ty = textureHeight - (int) (t * textureHeight);
            texture.getPixel(tx, ty, color);
        }
        else {
            color[0] = 0;
            color[1] = 0;
            color[2] = 0;
            color[3] = 0;
        }
        
        
        renderer.getColorBuffer().getPixel(x, y, destColor);

        double alpha = color[0] / 250.0;
        color[0] = 255;
        color[1] = (int) MathUtil.lerp(destColor[1], color[1], alpha);
        color[2] = (int) MathUtil.lerp(destColor[2], color[2], alpha);
        color[3] = (int) MathUtil.lerp(destColor[3], color[3], alpha);
        
        renderer.setPixel(x, y, color, depth);
    }

}
