package br.ol.eightball.physics2d;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Physics 2D (engine) class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Physics2D implements ContactListener {
    
    private final Vec2 gravity = new Vec2(0, 0);
    private final List<RigidBody> rigidBodies = new ArrayList<RigidBody>();
    private final List<ContactListener> contactListeners = new ArrayList<ContactListener>();
    private final List<Contact> contacts = new ArrayList<Contact>();
    private final List<Contact> contactsCache = new ArrayList<Contact>();
    
    public Physics2D() {
    }

    public Vec2 getGravity() {
        return gravity;
    }

    public List<RigidBody> getRigidBodies() {
        return rigidBodies;
    }
    
    public void addContactListener(ContactListener contactListener) {
        contactListeners.add(contactListener);
    }
    
    public void update() {
        // handle onCollisionOut
        Iterator<Contact> icontact = contacts.iterator();
        while (icontact.hasNext()) {
            Contact contact = icontact.next();
            RigidBody rb1 = contact.getRba();
            RigidBody rb2 = contact.getRbb();
            if (rb1.getShape() instanceof Circle && rb2.getShape() instanceof Circle 
                    && !CollisionDetection.checkCollisionCircleCircle(rb1, rb2, contact)) {
                
                icontact.remove();
                saveContactToCache(contact);
                onCollisionOut(contact);
            }
            else if (rb1.getShape() instanceof Circle && rb2.getShape() instanceof StaticLine 
                    && !CollisionDetection.checkCollisionCircleStaticLine(rb1, rb2, contact)) {
                
                icontact.remove();
                saveContactToCache(contact);
                onCollisionOut(contact);
            }
            else if (rb2.getShape() instanceof Circle && rb1.getShape() instanceof StaticLine 
                    && !CollisionDetection.checkCollisionCircleStaticLine(rb2, rb1, contact)) {
                
                icontact.remove();
                saveContactToCache(contact);
                onCollisionOut(contact);
            }
        }
        
        // handle onCollision & onCollisionEnter 
        for (RigidBody rb1 : getRigidBodies()) {
            for (RigidBody rb2 : getRigidBodies()) {
                if (rb1 != rb2 && rb1.isVisible() && rb2.isVisible()) {
                    Contact contact = getContactFromCache(rb1, rb2);
                    if (rb1.getShape() instanceof Circle && rb2.getShape() instanceof Circle 
                            && CollisionDetection.checkCollisionCircleCircle(rb1, rb2, contact)) {
                        
                        onCollision(contact);
                        if (!contacts.contains(contact)) {
                            contacts.add(contact);
                            onCollisionEnter(contact);
                        }
                    }
                    else if (rb1.getShape() instanceof Circle && rb2.getShape() instanceof StaticLine 
                            && CollisionDetection.checkCollisionCircleStaticLine(rb1, rb2, contact)) {
                        
                        onCollision(contact);
                        if (!contacts.contains(contact)) {
                            contacts.add(contact);
                            onCollisionEnter(contact);
                        }
                    }
                    else if (rb2.getShape() instanceof Circle && rb1.getShape() instanceof StaticLine 
                            && CollisionDetection.checkCollisionCircleStaticLine(rb2, rb1, contact)) {
                        
                        onCollision(contact);
                        if (!contacts.contains(contact)) {
                            contacts.add(contact);
                            onCollisionEnter(contact);
                        }
                    }
                }
            }
        }
        for (RigidBody body : getRigidBodies()) {
            body.updateVelocity();
        }
        //for (int i = 0; i < 4; i++) {
            for (Contact contact : contacts) {
                contact.resolveCollision();
            }
        //}
        for (RigidBody body : getRigidBodies()) {
            body.update();
        }
        for (Contact contact : contacts) {
            contact.correctPosition();
        }
    }

    public void draw(Graphics2D g) {
        for (RigidBody body : getRigidBodies()) {
            if (body.isVisible()) {
                body.draw(g);
            }
        }
//        for (Contact contact : contacts) {
//            contact.draw(g);
//        }
    }
    
    // ContactListener
    
    @Override
    public void onCollisionEnter(Contact contact) {
        for (ContactListener contactListener : contactListeners) {
            contactListener.onCollisionEnter(contact);
        }
    }

    @Override
    public void onCollision(Contact contact) {
        for (ContactListener contactListener : contactListeners) {
            contactListener.onCollision(contact);
        }
    }

    @Override
    public void onCollisionOut(Contact contact) {
        for (ContactListener contactListener : contactListeners) {
            contactListener.onCollisionOut(contact);
        }
    }
    
    // contacts cache
    
    public Contact getContactFromCache(RigidBody rba, RigidBody rbb) {
        if (contactsCache.isEmpty()) {
            return new Contact(rba, rbb);
        }
        Contact contact = contactsCache.remove(contactsCache.size() - 1);
        contact.setRba(rba);
        contact.setRbb(rbb);
        return contact;
    }
    
    public void saveContactToCache(Contact contact) {
        contactsCache.add(contact);
    }
 
}
