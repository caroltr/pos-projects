using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class MenuConfig : MonoBehaviour {

    public static GameObject jogador;

    [SerializeField]
    [Tooltip("Referencia para o menu \"Fim\"")]
    private GameObject panelFim;

    [SerializeField]
    [Tooltip("Referencia para o menu \"Pause\"")]
    private GameObject panelPause;

    [SerializeField]
    [Tooltip("Referencia para o painel que armazena componentes da UI")]
    private GameObject panelForeground;

    // Use this for initialization
    void Start()
    {
        AdController.menu = this;
        Player.menu = this;

        Button btnContinue = getButtonContinueFromFimPanel();
        if(btnContinue != null) {
            handleContinueButtonText(btnContinue);
        } else {
            Debug.Log("Button continue is null");
        }
    }

    public void ClicaBotaoIniciar()
    {
        SceneManager.LoadScene("SampleScene");
        AdController.ShowStartGameAd();
    }

    public void ClicaBotaoContinuar(bool isPaused)
    {
        // Se menu está pausado, nao é neessário abrir tela de ad.
        // Se é menu "Fim de jogo", ad de reward aparece para ele voltar a jogar,
        // se assistir a propaganda


        if (!isPaused)
        {
            AdController.ShowRewardAd();
        }
    }

    public void ClicaBotaoCreditos()
    {
        SceneManager.LoadScene("Creditos");
    }

    public void ClicaBotaoMenu()
    {
        SceneManager.LoadScene("TelaPrincipal");
    }

    private IEnumerable handleContinueButtonText(Button btnContinue) {
        
        var btnText = btnContinue.GetComponentInChildren<Text>();

        while (true)
        {
            if (AdController.nextTimeShowAd.HasValue &&
                (DateTime.Now < AdController.nextTimeShowAd))
            {

                Debug.Log("IF");
                btnText.text = "time";

                btnContinue.interactable = false;

                TimeSpan restante = AdController.nextTimeShowAd.Value - DateTime.Now;
                var contagemRegressiva = string.Format("{0:D2}:{1:D2}", restante.Minutes, restante.Seconds);

                Debug.Log("contagemRegressiva: " + contagemRegressiva);

                btnText.text = contagemRegressiva;

                // Esperar ao menos 1 segundo 
                yield return new WaitForSeconds(1f);

            }
            else
            {
                Debug.Log("ELSE");

                btnContinue.interactable = true;
                btnContinue.onClick.AddListener(AdController.ShowRewardAd);

                //AdController.obstaculo = this;
                btnText.text = "Continuar";

                break;
            }
        }
    }

    private Button getButtonContinueFromFimPanel() {

        GameObject go = GameObject.Find("CanvasMenu").transform
                                  .Find("PanelFim").transform
                                  .Find("Panel").transform.gameObject;
        
        var botoes = go.transform.GetComponentsInChildren<Button>();
        Button botaoContinue = null;

        foreach (var botao in botoes)
        {
            if (botao.gameObject.name.Equals("btnContinuar"))
            {
                botaoContinue = botao;
                break;
            }
        }

        return botaoContinue;
    }


    public void ExibirMenuFim()
    {
        Debug.Log("Exibir Menu FIM");

        panelFim.SetActive(true);
        jogador.SetActive(false);

        SetActiveForegroundUI(false);
    }

    public void ContinueGame()
    {
        Debug.Log("Continuar Game");

        panelFim.SetActive(false);
        panelPause.SetActive(false);

        jogador.SetActive(true);
        Player.life = 1;

        SetActiveForegroundUI(true);
    }

    private void SetActiveForegroundUI(bool isActive)
    {
        panelForeground.SetActive(isActive);
    }
}
