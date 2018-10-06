using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Hint : MonoBehaviour {

    [SerializeField]
    [Tooltip("Referencia ao texto de dica, ao aproximar da gaiola onde o amigo está preso")]
    public GameObject textoDica;

    public Collider2D collider2D;

    // Use this for initialization
    void Start () {
    }

    // Update is called once per frame
    void Update () {
		
	}

    private void OnTriggerExit2D(Collider2D other)
    {
        collider2D.isTrigger=false;
        textoDica.gameObject.SetActive(true);
    }

    public void botaoVitoriaVoltaMenu()
    {
        SceneManager.LoadScene("TelaPrincipal");
    }
}
