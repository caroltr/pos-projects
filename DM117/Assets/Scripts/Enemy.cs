using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Enemy : MonoBehaviour
{
    [SerializeField]
    [Tooltip("Referencia ao inimigo")]
    private GameObject enemy;
    private Rigidbody2D enemyRB;

    //Velocidade inimigo
    float enemySpeed = 2.0f;
    float tempo = 0f;

    // Use this for initialization
    void Start () {
        enemyRB = GetComponent<Rigidbody2D>();
    }

    
    void FixedUpdate()
    {
        enemyRB.velocity = new Vector2(enemySpeed, 0.0f);
    }

    // Update is called once per frame
    void Update () {
	}

    //Caso a particula do plauer(tiro de arma?) relar no inimigo, inimigo morre(some)
    void OnTriggerStay2D(Collider2D collider)
    {
        if (collider.gameObject.tag == "particle")
        {
            print("colidiu");
            transform.gameObject.SetActive(false);
        }
    }

    //Se colidir com qualquer objeto que nao seja o player, ele vira
    void OnTriggerExit2D(Collider2D collision)
    {
        if (!collision.CompareTag("Player"))
        {
            FlipSprite();
        }
    }

    void FlipSprite()
    {
        transform.localScale = new Vector3(-Mathf.Sign(enemyRB.velocity.x), 1, 1);
        enemySpeed *= -1;
    }
}
