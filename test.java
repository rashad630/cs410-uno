package cs410.uno;

import java.util.ArrayList;
import java.util.Collections;

public class test {

    public static void main(String[] args)
    {
        GameState test = GameState.startGame(5, 7, 2,2 , 8);

        while (true)
        {
            test.runOneTurn();
        }

    }
}
