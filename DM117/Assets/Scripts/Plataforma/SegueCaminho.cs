using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SegueCaminho : MonoBehaviour {

    public DefineCaminho caminho;
    public float vel = 1;
    public float maxDist = .1f;

    private IEnumerator<Transform> ponto;


    // Use this for initialization
    void Start()
    {
        if (caminho == null)
        {
            Debug.LogError("Path cannot be null", gameObject);
            return;
        }

        ponto = caminho.GetPathEnumerator();
        ponto.MoveNext();

        if (ponto.Current == null)
            return;

        transform.position = ponto.Current.position;

    }
    // Update is called once per frame
    public void Update()
    {
        if (ponto == null || ponto.Current == null) //Verifica que exista ao menos 1 ponto definido
            return;
        transform.position = Vector3.MoveTowards(transform.position, ponto.Current.position, Time.deltaTime * vel);
        float dist = Vector3.Distance(transform.position, ponto.Current.position);
        if (dist < maxDist)
            ponto.MoveNext();
    }

}
