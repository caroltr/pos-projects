using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CamPlayer : MonoBehaviour {

    [SerializeField]
    [Tooltip("Referencia ao chão")]
    public GameObject chao;
    [SerializeField]
    [Tooltip("Referencia ao player")]
    public Transform player;

    public float offset, x;

    // Use this for initialization
    void Start () {
        player = GameObject.FindGameObjectWithTag("Player").transform;
        x = -13.45f;
        while (x < 0)
        {
            x += 0.19f;
        }
    }
	
	// Update is called once per frame
	void Update () {
        transform.rotation = Quaternion.identity;
        transform.position = player.position - Vector3.forward * 5 + Vector3.right * offset;
    }
}
