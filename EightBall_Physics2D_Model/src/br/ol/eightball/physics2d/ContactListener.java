package br.ol.eightball.physics2d;

/**
 *
 * @author leonardo
 */
public interface ContactListener {

    public void onCollisionEnter(Contact contact);
    public void onCollision(Contact contact);
    public void onCollisionOut(Contact contact);
    
}
