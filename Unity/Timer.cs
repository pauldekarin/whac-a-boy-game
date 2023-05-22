using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Timer : MonoBehaviour
{
	
	
	private int duration;
	private float time;
		
	private Vector3 vecSpin;
	
	private Transform handTransform;
	private Quaternion handRotation;
	
	
	void setDurationAS(string _duration){
		this.duration = int.Parse(_duration);

		handTransform.rotation = handRotation;
		handTransform.Rotate(vecSpin*(60 - this.duration));
		
		
	}
    void Start()
    {
		time = 0;
		duration = 60;
		
		vecSpin = new Vector3(0,0,1)*360/60;
		
		handTransform = gameObject.transform.GetChild(4).gameObject.transform;
		handRotation = handTransform.rotation;
		setDurationAS("45");
    }
	
	void Spin(){
		time += Time.deltaTime;

		if (time >= duration){
			GameObject.FindObjectOfType<GameLogic>().setPlaying(false);
		}
		else{
			handTransform.Rotate(vecSpin*Time.deltaTime);
		}
	}
	
    void Update()
    {
		Spin();
    }
}
