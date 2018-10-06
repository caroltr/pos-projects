using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Player : MonoBehaviour {

    public static MenuConfig menu;

    [SerializeField]
    Vector2 playerJump;
    
    [SerializeField]
    float playerRun;


    private bool estaNoChao;
    float gravity;

    Rigidbody2D rb;
    Animator playerAnimator;
    CapsuleCollider2D playerCollider;

    float playerXDir;
    float playerYDir;

    public static int life = 3;
    
    void Start () {

        MenuConfig.jogador = this.gameObject;

        rb = GetComponent<Rigidbody2D>();
        playerAnimator = GetComponent<Animator>();
        playerCollider = GetComponent<CapsuleCollider2D>();
        gravity = rb.gravityScale;

    }

    void Update () {

        playerXDir = Input.GetAxis("Horizontal");
        playerYDir = Input.GetAxis("Vertical");
    }

    void FixedUpdate()
    {
        Run();

        if (Input.GetButtonDown("Jump")&& playerCollider.IsTouchingLayers(
                LayerMask.GetMask("ground")))
        {
           Jump();
        }
    }
    /// <summary>
    /// Metodo para que o player corra
    ///</summary>
    void Run()
    {
        rb.velocity = new Vector2(playerXDir * playerRun, rb.velocity.y);

        if (playerXDir != 0)
        {
            //FlipSprite();
            playerAnimator.SetBool("playerRunning", true);
        }
        else
        {
            playerAnimator.SetBool("playerRunning", false);
        }
    }

    /// <summary>
    /// Metodo para que o player pule, chamado em FixedUpdate().
    /// Caso player esteja no chão e a tecla 'Jump'(space) tenha sido pressionada
    ///</summary>
    void Jump()
    {
        playerJump.x *= Mathf.Sign(rb.velocity.x);
        rb.velocity += playerJump;
    }

    void OnCollisionEnter2D(Collision2D obj) {

        Debug.Log("Colision!");

        if(obj.gameObject.name.StartsWith("spike"))
        {
            Debug.Log("Colision is spike!");
            life -= 1;

            print("Life now is " + life);

            if(life <= 0) {
                Debug.Log("morreu");
                menu.ExibirMenuFim();

            }
        }
    }
}
