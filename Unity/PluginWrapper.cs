using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PluginWrapper : MonoBehaviour
{
	
    // Start is called before the first frame update
    void Start()
    {
		AndroidJavaClass unityClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject unityActivity = unityClass.GetStatic<AndroidJavaObject>("currentActivity");
		AndroidJavaClass javaClass = new AndroidJavaClass("com.example.unityplugin.PluginHandler");
		
		javaClass.CallStatic("Launch", unityActivity);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
