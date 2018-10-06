using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class HandleContinueButton : MonoBehaviour {

	// Use this for initialization
	void Start () {

        // Button btnObj = GameObject.Find("btnContinuar").GetComponent

        GameObject go = GameObject.Find("Canvas").transform.gameObject;
        var botoes = go.transform.GetComponentsInChildren<Button>();
        Button botaoContinue = null;

        foreach (var botao in botoes)
        {
            if (botao.gameObject.name.Equals("BotaoContinue"))
            {
                botaoContinue = botao;
                break;
            }
        }

        Debug.Log("botaoContinue is null?? " + (botaoContinue == null ? "sim" : "nao"));
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
