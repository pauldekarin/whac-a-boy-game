using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class TouchEvent : MonoBehaviour
{
	private GameLogic gameLogic;
	
    void Start()
    {
        gameLogic = GameObject.FindObjectOfType<GameLogic>();
    }

    void Update()
    {
		if (Input.GetMouseButtonDown(0)){
			Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
			RaycastHit hit;
			
			if (Physics.Raycast(ray, out hit)){
			
				if (hit.transform.tag == "Character"){
					gameLogic.onTouch(hit.transform.name);
				}
				
			}
		}
        if(Input.touchCount > 0 && Input.touches[0].phase == TouchPhase.Began){
			Ray ray = Camera.main.ScreenPointToRay(Input.touches[0].position);
			RaycastHit hit;
			
			if (Physics.Raycast(ray, out hit)){
				if (hit.transform.tag == "Character"){
					gameLogic.onTouch(hit.transform.name);
				}
			}
		}
    }
    
}
