using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AnimatorListener : MonoBehaviour
{
	private GameLogic gameLogic;
	
    void Start()
    {
        gameLogic = GameObject.FindObjectOfType<GameLogic>();
    }


    
    void playNext(){
		gameLogic.playNext();
	}
	
	void onEndBoo(){
		gameLogic.onEndBoo();
	}
}
