using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Rendering;
using TMPro;

public class GameLogic : MonoBehaviour
{
	
	public TMP_Text scoreText;
	
	public GameObject character;
	public GameObject table;
	public GameObject chair;
	
	public float xDist;
	public float zDist;
	
	
	private Animator[] animators = new Animator[6];
	
	private AudioSource hitSound;
	private AudioSource bellSound;
	
	private int currentId;
	private int nextId;
	private int score;
	private int totalBoo;
	
	private bool attacked;
	private bool isPlaying;
	
	
	private AndroidJavaClass unityClass;
	private AndroidJavaObject unityActivity;
	private AndroidJavaClass javaClass;	
	public void setSpeedAS(string speed){
		foreach (Animator animator in animators){
			animator.speed = float.Parse(speed);
		}
		
	}
	
	void Awake(){
		score = 0;
		totalBoo = 0;
		currentId = 0;
		nextId = 0;
		
		attacked = false;
		isPlaying = true;
		
		hitSound = GetComponents<AudioSource>()[0];
		hitSound.volume = 0.2f;
		
		bellSound = GetComponents<AudioSource>()[1];
		bellSound.volume = 0.2f;
		
		unityClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		unityActivity = unityClass.GetStatic<AndroidJavaObject>("currentActivity");
		javaClass = new AndroidJavaClass("com.example.unityplugin.PluginHandler");
		
		Application.targetFrameRate = 60;
		
		
		
	}
	
    void Start()
    {	
	
		int i = 0;
		for (int y = 0; y < 2; y++){
			for (int x = -1; x < 2; x++){
				Vector3 vecPos = new Vector3(xDist*x,0,zDist*y) + gameObject.transform.position;
				
				GameObject _character = Instantiate(character, vecPos,Quaternion.identity);
				GameObject _table = Instantiate(table, vecPos,Quaternion.identity);
				GameObject _chair = Instantiate(chair, vecPos,Quaternion.identity);
				
				_character.transform.name = i.ToString();
				
				animators[i] = _character.GetComponent<Animator>();
				animators[i].speed = 1.4f;
				i++;
				
			}
		}
		
		bellSound.Play();
		Invoke("boo", bellSound.clip.length);
		javaClass.CallStatic("Start");
	}
	
	public void setPlaying(bool play){
		isPlaying = play;
	}
	
	public void playNext(){
		if (isPlaying) boo();
		else javaClass.CallStatic("Stop",unityActivity,score,totalBoo);
	}
	
	
	public void onEndBoo(){
		currentId = nextId;
		attacked = false;
	}
	
	
	public void onTouch(string touchedTag){
		if (!attacked){
			int touchedId;
			if (int.TryParse(touchedTag,out touchedId)){
				Debug.Log(currentId.ToString());
				if (touchedId == currentId){
					score++;
					attacked = true;
					scoreText.text = score.ToString();
					
					hitSound.Play();
				}
			}
		}
	}
	
	
	private void boo(){
		
		int next = Random.Range(0,6);
		while (next == nextId) next = Random.Range(0,6);
		nextId = next;
		
		
		animators[nextId].Play("ACTION");
		
		totalBoo++;
		
	}
	
	public void setText(string tex){
		scoreText.text = tex;
	}
	public void setInt(int it){
		
		scoreText.text = it.ToString();
	}
	
	
    void Update()
    {
		
    }
}
