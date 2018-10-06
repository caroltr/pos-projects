using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class MenuPause : MonoBehaviour
{

    public static bool pausado;

    [SerializeField]
    [Tooltip("Referencia para o menu pause")]
    private GameObject menuPause;

    [SerializeField]
    [Tooltip("Referencia para o menu canvas")]
    private GameObject canvasMenu;

    /// <summary>
    /// Metodo para reiniciar a tela do jogo
    /// </summary>
    public void restart()
    {
        Time.timeScale = 1;
        SceneManager.LoadScene(SceneManager.GetActiveScene().name);
    }

    /// <summary>
    /// Metodo para pausar/despausar o jogo
    /// </summary>
    /// <param name="isPausado">If set to <c>true</c> is pausado.</param>
    public void SetMenuPause(bool isPausado)
    {
        pausado = isPausado;

        // Se o jogo estiver pausado, timeScale recebe 0
        Time.timeScale = pausado ? 0 : 1;

        // Deixa o menu-panel pausado
        menuPause.SetActive(pausado);
        canvasMenu.SetActive(pausado);
    }

    /// <summary>
    /// Metodo para carregar scenes
    /// </summary>
    /// <param name="nomeScene">Nome scene.</param>
    public void CarregaScene(string nomeScene)
    {
        SceneManager.LoadScene(nomeScene);
    }

    // Use this for initialization
    void Start()
    {
        pausado = false;
    }
}
