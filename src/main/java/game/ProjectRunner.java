// Virginia Tech Honor Code Pledge:
//
// This is a submission for project 2 (Spring 2025)
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of
// those who do.
// -- Kyler O'Rourke (korourke) 
 
package game;

public class ProjectRunner {
    public static void main(String[] args) {
        WhackAShape game;
        if (args.length > 0) {
            game = new WhackAShape(args);
        } else {
            game = new WhackAShape();
        }
        game.show();
    }
}
