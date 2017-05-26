package br.ol.eightball.math;

/**
 *
 * @author leonardo
 */
public class Mat4 {

    public double m00, m01, m02, m03;
    public double m10, m11, m12, m13;
    public double m20, m21, m22, m23;
    public double m30, m31, m32, m33;

    public Mat4() {
    }

    public void set(Mat4 m) {
        m00 = m.m00; m01 = m.m01; m02 = m.m02; m03 = m.m03;
        m10 = m.m10; m11 = m.m11; m12 = m.m12; m13 = m.m13;
        m20 = m.m20; m21 = m.m21; m22 = m.m22; m23 = m.m23;
        m30 = m.m30; m31 = m.m31; m32 = m.m32; m33 = m.m33;
    }

    public void setIdentity() {
        m01 = m02 = m03 = 0;
        m10 = m12 = m13 = 0;
        m20 = m21 = m23 = 0;
        m30 = m31 = m32 = 0;
        m00 = m11 = m22 = m33 = 1;
    }

    public void setPerspectiveProjection(double fov, double screenWidth) {
        double d = (screenWidth * 0.5) / Math.tan(fov * 0.5);
        setIdentity();
        m32 = -1 / d;
        m33 = 0;
    }

    public void setScale(double sx, double sy, double sz) {
        setIdentity();
        m00 = sx;
        m11 = sy;
        m22 = sz;
        m33 = 1;
    }
    
    public void setTranslation(double x, double y, double z) {
        setIdentity();
        m03 = x;
        m13 = y;
        m23 = z;
    }
    
    public void setRotationX(double angle) {
        setIdentity();
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        m11 =  c; m12 = s;
        m21 = -s; m22 = c;
    }

    public void setRotationY(double angle) {
        setIdentity();
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        m00 = c; m02 = -s;
        m20 = s; m22 =  c;
    }

    public void setRotationZ(double angle) {
        setIdentity();
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        m00 =  c; m01 = s;
        m10 = -s; m11 = c;
    }
    
    public void multiply(Mat4 m) {
        double nm00 = m00 * m.m00 + m01 * m.m10 + m02 * m.m20 + m03 * m.m30;
        double nm01 = m00 * m.m01 + m01 * m.m11 + m02 * m.m21 + m03 * m.m31;
        double nm02 = m00 * m.m02 + m01 * m.m12 + m02 * m.m22 + m03 * m.m32;
        double nm03 = m00 * m.m03 + m01 * m.m13 + m02 * m.m23 + m03 * m.m33;
        double nm10 = m10 * m.m00 + m11 * m.m10 + m12 * m.m20 + m13 * m.m30;
        double nm11 = m10 * m.m01 + m11 * m.m11 + m12 * m.m21 + m13 * m.m31;
        double nm12 = m10 * m.m02 + m11 * m.m12 + m12 * m.m22 + m13 * m.m32;
        double nm13 = m10 * m.m03 + m11 * m.m13 + m12 * m.m23 + m13 * m.m33;
        double nm20 = m20 * m.m00 + m21 * m.m10 + m22 * m.m20 + m23 * m.m30;
        double nm21 = m20 * m.m01 + m21 * m.m11 + m22 * m.m21 + m23 * m.m31;
        double nm22 = m20 * m.m02 + m21 * m.m12 + m22 * m.m22 + m23 * m.m32;
        double nm23 = m20 * m.m03 + m21 * m.m13 + m22 * m.m23 + m23 * m.m33;
        double nm30 = m30 * m.m00 + m31 * m.m10 + m32 * m.m20 + m33 * m.m30;
        double nm31 = m30 * m.m01 + m31 * m.m11 + m32 * m.m21 + m33 * m.m31;
        double nm32 = m30 * m.m02 + m31 * m.m12 + m32 * m.m22 + m33 * m.m32;
        double nm33 = m30 * m.m03 + m31 * m.m13 + m32 * m.m23 + m33 * m.m33;
        m00 = nm00; m01 = nm01; m02 = nm02; m03 = nm03;
        m10 = nm10; m11 = nm11; m12 = nm12; m13 = nm13;
        m20 = nm20; m21 = nm21; m22 = nm22; m23 = nm23;
        m30 = nm30; m31 = nm31; m32 = nm32; m33 = nm33;
    }

    public void multiply(Vec4 v) {
        double nx = m00 * v.x + m01 * v.y + m02 * v.z + m03 * v.w;
        double ny = m10 * v.x + m11 * v.y + m12 * v.z + m13 * v.w;
        double nz = m20 * v.x + m21 * v.y + m22 * v.z + m23 * v.w;
        double nw = m30 * v.x + m31 * v.y + m32 * v.z + m33 * v.w;
        v.x = nx;
        v.y = ny;
        v.z = nz;
        v.w = nw;
    }
    
    // http://www.cg.info.hiroshima-cu.ac.jp/~miyazaki/knowledge/teche23.html
    public void invert() {
        // TODO
    }
    
}
