using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Gaiola : MonoBehaviour {

    [SerializeField]
    [Tooltip("Referencia ao player")]
    public Transform player;
    [SerializeField]
    [Tooltip("Referencia ao amigo a ser salvo")]
    public Transform friend;
    [SerializeField]
    [Tooltip("Referencia ao texto de dica, ao aproximar da gaiola onde o amigo está preso")]
    public GameObject text;
    
    // Use this for initialization
    void Start () {
        player = GameObject.FindGameObjectWithTag("Player").transform;
        friend = GameObject.FindGameObjectWithTag("Friend").transform;
    }
	
	// Update is called once per frame
	void Update () {
       
        
    }

    void OnTriggerStay2D(Collider2D other)
    {
        if (other.gameObject.tag == "Player")
        {
            if (Input.GetMouseButton(0))
            {
                AbrirGaiola(Input.mousePosition);
                print("fui tocado");
                player.transform.position = new Vector3(-13, 5.3f, -10);
                friend.transform.position = new Vector3(-13, 5.3f, -10);
                text.SetActive(false);
            }
        }

    }

    private static void AbrirGaiola(Vector3 position)
    {
        Ray raycast = Camera.main.ScreenPointToRay(position);
        RaycastHit hit;

        if (Physics.Raycast(raycast, out hit))
        {
            hit.transform.SendMessage("GaiolaTocada", SendMessageOptions.DontRequireReceiver);
            print(hit.transform.name);
        }
    }


}
