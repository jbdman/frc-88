/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author tj2
 */
public class Arm {
    // comment to see if SVN works

    private Solenoid m_elbow;
    private Solenoid m_gripper;
    private Solenoid m_pusher;

    private boolean m_isClosed;
    private boolean m_isDown;
    private boolean m_isOut;

    public Arm(int elbowChannel, int gripperChannel, int pusherChannel) {

        m_elbow = new Solenoid(elbowChannel);
        m_gripper = new Solenoid(gripperChannel);
        m_pusher = new Solenoid(pusherChannel);

        m_isClosed = false;
        m_isDown = false;
        m_isOut = false;
    }

    public void punch(boolean out) {
        if(out != m_isOut) {
            m_pusher.set(out);
        }
        m_isOut = out;
    }

    public void drop(boolean down) {
        if(down != m_isDown) {
            m_elbow.set(down);
        }
        m_isDown = down;
    }

    public void release(boolean close) {
        if(close != m_isClosed) {
            m_gripper.set(close);
        }
        m_isClosed = close;
    }
}
