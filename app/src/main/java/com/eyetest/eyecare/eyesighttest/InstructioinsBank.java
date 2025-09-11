package com.eyetest.eyecare.eyesighttest;

import java.util.Locale;

public class InstructioinsBank {

    public static String getInstructions(String exerciseType) {
        if (exerciseType == null) return getDefaultInstructions();

        switch (exerciseType.toLowerCase(Locale.ROOT)) {
            case "snellen_chart":
                return  "1) Cover one eye with your hand.\n"
                        + "2) Please read the letters shown on the screen from left to right and select a correct sequence\n"
                        + "3) Repeat the process for the other eye.\n\n";

            case "eye_muscle_training":
                return "1) Sit in a comfortable position and keep your head still.\n" +
                        "2) Follow the moving object or pattern on the screen using only your eyes.\n" +
                        "3) Complete each exercise slowly and with full focus.\n" +
                        "4) Repeat as guided to help strengthen and relax your eye muscles.";

            case "contrast_sensitivity":
                return "1) Sit in a well-lit room with minimal glare on your screen.\n" +
                        "2) Make sure your screen brightness is set to a comfortable, standard level.\n" +
                        "3) Tap on “Start Test”.\n" +
                        "4) You will be shown various shapes or lines with different contrast levels.\n" +
                        "5) Identify the faint shapes or patterns and select the correct options.\n" +
                        "6) Try not to squint or adjust your distance during the test.\n" +
                        "7) Complete all test steps to get your result.";

            case "landolt":
                return "1) Sit in a quiet room with good lighting.\n" +
                        "2) Keep your device at eye level and about 14–16 inches away.\n" +
                        "3) Tap on “Start Test”.\n" +
                        "4) You will see rings with a gap (like a \"C\") in various positions (up, down, left, right, etc.).\n" +
                        "5) Identify the direction of the gap for each ring shown.\n" +
                        "6) Select the direction you see (e.g., top, right, bottom, left).\n" +
                        "7) Proceed through all levels to complete the test.";
            case "color_blindness":
                return "1) Observe the image or pattern shown on the screen.\n"
                        + "2) Identify the number or shape hidden within the colored dots.\n"
                        + "3) Select the correct answer from the given options.\n"
                        + "3) Repeat for all the test patterns to complete the assessment.\n";
            case "astigmatism_test":
                return "1) Sit at a comfortable distance from the screen (about 14–16 inches for mobile).\n" +
                        "2) Ensure even lighting in the room and reduce glare on your screen.\n" +
                        "3) Tap on “Start Test”.\n" +
                        "4) You will see lines in different directions and patterns.\n" +
                        "5) Focus on the image and note if any lines appear darker, blurrier, or more prominent than others.\n" +
                        "6) Answer as accurately as possible based on what you see.\n" +
                        "7) The result will indicate the likelihood of astigmatism.";
            case "okn_test":
                return "1) Sit comfortably in a quiet, distraction-free area.\n" +
                        "2) Make sure the screen is at eye level.\n" +
                        "3) Maintain a normal room light (not too bright or too dim).";
            case "visual_acuity":
                return "1) \"Measure How Clearly You Can See Objects from a Distance\"\n" +
                        "2) \"Begin Your Eye Check by Spotting Letters That Gradually Get Smaller\"\n" +
                        "3) \"Check the Clarity of Your Vision with a Simple On-Screen Test\"\n" +
                        "4) \"Find Out How Sharp Your Eyes Are with Our Visual Acuity Assessment\"";
            case "hyperopia_prevention":
                return "1) Sit in a well-lit space and hold your screen at eye level.\n" +
                        "2) Follow the guided focus exercises to shift between near and far objects.\n" +
                        "3) Avoid straining—keep your eyes relaxed during each step.\n" +
                        "4) Repeat the routine regularly to support long-distance vision health.";

            case "focus_shift":
                return "1) Sit comfortably and keep your head still.\n" +
                        "2) Focus on a nearby object, then slowly shift your gaze to a distant one.\n" +
                        "3) Hold each focus point for a few seconds before switching.\n" +
                        "4) Repeat the process as guided to improve focus flexibility.";

            case "lazy_eye":
                return "1) Cover your stronger eye to make the weaker (lazy) eye work harder.\n" +
                        "2) Follow the on-screen exercises carefully using only the uncovered eye.\n" +
                        "3) Focus on moving objects or shapes to improve eye coordination.\n" +
                        "4) Practice daily for best results and stronger visual response.";

            case "closed_eye_movements":
                return "1) Sit in a relaxed position and gently close your eyes.\n" +
                        "2) Take slow, deep breaths to help your eyes and mind relax.\n" +
                        "3) Stay in this calm state for the guided duration.\n" +
                        "4) Use this moment to reduce eye strain and refresh your vision.";

            case "kaleidoscope":
                return "1) Gaze at the moving kaleidoscope patterns on your screen.\n" +
                        "2) Follow the shapes and colors with your eyes without moving your head.\n" +
                        "3) Stay focused and relaxed as the patterns shift.\n" +
                        "4) This visual stimulation helps improve focus and eye movement control.\n";

            case "palming":
                return "1) Cover your stronger eye to make the weaker (lazy) eye work harder.\n" +
                        "2) Follow the on-screen exercises carefully using only the uncovered eye.\n" +
                        "3) Focus on moving objects or shapes to improve eye coordinRub your hands together to warm them slightly.\n" +
                        "4) Gently cover your closed eyes with your palms without applying pressure.\n" +
                        "5) Relax and breathe deeply while resting your eyes in darkness.\n" +
                        "6) Hold this position for the guided time to relieve eye strain and tension.ation.\n" +
                        "7) Practice daily for best results and stronger visual response.";
            case "track_animal":
                return "1) Watch the screen and spot the animal as it begins to move.\n" +
                        "2) Follow the moving animal using only your eyes, not your head.\n" +
                        "3) Keep your focus steady as the speed or direction changes.\n" +
                        "4) This game helps improve eye tracking and coordination.";
            case "color_cube":
                return "1) Observe the screen carefully and identify the cube with the different color.\n" +
                        "2) Tap or select the odd-colored cube as quickly as possible.\n" +
                        "3) Each level may increase in speed or complexity.\n" +
                        "4) This game sharpens color recognition and visual attention.";
            case "color_match":
                return "1) Focus on the color shown at the top of the screen.\n" +
                        "2) Select the matching color from the given options below.\n" +
                        "3) Act quickly—colors may change with time or level.\n" +
                        "4) This game improves color perception and reaction speed.";
            case "odd_one_out":
                return "1) Look carefully at the group of shapes, colors, or symbols on the screen.\n" +
                        "2) Identify the item that looks different from the rest.\n" +
                        "3) Tap the odd one as quickly and accurately as you can.\n" +
                        "4) This game boosts visual scanning and attention to detail.";

            // add more cases as needed
            default:
                return getDefaultInstructions();
        }
    }

    private static String getDefaultInstructions() {
        return "Follow the on-screen instructions for this exercise. Tap Start Test to begin.";
    }
}
