using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GunPlayer : MonoBehaviour {
    [SerializeField]
    [Tooltip("Referencia para a particula de 'tiro' do player")]
    public ParticleSystem particle;
	// Use this for initialization
	void Start () {
		
	}

    // Update is called once per frame
    /// <summary>
    //Inicia particula ao clicar com o botao direito do mouse
    ///</sumary>
    void FixedUpdate () {
        if (Input.GetMouseButton(1))
        {
            particle.Play();
        }
        else
        {
            particle.Stop();
        }

	}
}
