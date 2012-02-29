/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *  This is for flashing lights and the camera.
 *  @author Edge
 */
public class Tracking extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private AxisCamera camera;
    private CriteriaCollection filterCriteria;

    public Tracking() {
        camera = AxisCamera.getInstance();
        camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedFlour1);
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        camera.writeBrightness(20);
        filterCriteria = new CriteriaCollection();
        filterCriteria.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 20, 200, false);
        filterCriteria.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 20, 200, false);
    }

    public void processImage() {

        final int HUE_LO = 60;
        final int HUE_HI = 140;
        final int SAT_LO = 60;
        final int SAT_HI = 255;
        final int LUM_LO = 60;
        final int LUM_HI = 255;
        
        if (camera.freshImage()) {

            try {
                ColorImage image = camera.getImage();
                BinaryImage colorMask = image.thresholdHSL(HUE_LO, HUE_HI, SAT_LO, SAT_HI, LUM_LO, LUM_HI);
                BinaryImage erodedImage = colorMask.removeSmallObjects(false, 2);
                BinaryImage convexHullImage = erodedImage.convexHull(false);
//                BinaryImage filteredImage = convexHullImage.particleFilter(filterCriteria);
                ParticleAnalysisReport p;

//                ParticleAnalysisReport[] particles = filteredImage.getOrderedParticleAnalysisReports(4);
                ParticleAnalysisReport[] particles = convexHullImage.getOrderedParticleAnalysisReports();

                if(particles.length > 0) {
                    // find highest (i.e. lowest y)
                    int index = -1;
                    double lowestY = 1.0;
                    for(int i = 0; i < particles.length; i++) {
                        p = particles[i];
                        if(p.boundingRectHeight * p.boundingRectWidth > 100) {
                            if(p.center_mass_y_normalized < lowestY) {
                                index = i;
                            }
                            System.out.println(i + ": " + p.center_mass_x_normalized + ", " +
                                    p.center_mass_y_normalized + " " + p.boundingRectHeight);
                        }
                    }
                    if(index >= 0) {
                        // report target angle and height->distance
                        p = particles[index];
                        double angle = p.center_mass_x_normalized * 22; // FIX THE MAGIC NUMBER!
                        double distance = 480.0/p.boundingRectHeight; // FIX THE MAGIC NUMBER!
                        SmartDashboard.putDouble("Target Angle", angle);
                        SmartDashboard.putDouble("Target Distance", distance);
                    }
                }
//                filteredImage.free();
                convexHullImage.free();
                erodedImage.free();
                colorMask.free();
                image.free();


            } catch (NIVisionException ex) {
                System.out.println("Camera NI exception");
            } catch (AxisCameraException ex) {
                System.out.println("Camera getImage() exception");
            }
        }
        
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}