using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MessageWin : MonoBehaviour {

    [SerializeField]
    [Tooltip("Referencia ao texto de vitoria")]
    public GameObject textoWin;

    [SerializeField]
    [Tooltip("Referencia ao texto de dica, ao aproximar da gaiola onde o amigo está preso")]
    public GameObject textoDica;

    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
    private void OnTriggerEnter2D(Collider2D other)
    {
        textoWin.gameObject.SetActive(true);
        textoDica.gameObject.SetActive(false);
    }

}
