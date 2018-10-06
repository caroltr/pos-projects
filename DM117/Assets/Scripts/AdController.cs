using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Advertisements;
using UnityEngine.SceneManagement;
using System;

public class AdController : MonoBehaviour {

    // Referencia para o obstaculo.
    public static MenuConfig menu;

	public static bool showAds = true;

    // Variavel para controlar o tempo
	public static DateTime? nextTimeShowAd = null;

	public static void ShowStartGameAd() {

        // Opcoes para o Ad
        ShowOptions options = new ShowOptions();
        options.resultCallback = Unpause;

#if UNITY_ADS
        // Exibe anuncio
        if(Advertisement.IsReady()) {
			Advertisement.Show(options);
		}
#endif

		// MenuPause.pausado = true;
		Time.timeScale = 0f;
	}

	public static void Unpause(ShowResult result) {
		// MenuPause.pausado = false;
		Time.timeScale = 1f;
	}

	public static void ShowRewardAd() {

		nextTimeShowAd = DateTime.Now.AddSeconds(10);

#if UNITY_ADS
        // Exibe anuncio
        if(Advertisement.IsReady()) {

			// MenuPause.pausado = true;
			Time.timeScale = 0f;

			ShowOptions options = new ShowOptions {
				resultCallback = handleAddResult
			};
			Advertisement.Show(options);
		}
#endif
	}

	public static void handleAddResult(ShowResult result) {

		switch(result) {
			case ShowResult.Finished:
				Debug.Log("Ad assistido");
                // TODO continuar acao -> obstaculo.Continue();

                menu.ContinueGame();

				break;

			case ShowResult.Skipped:
                Debug.Log("Ad pulado");
				break;

			case ShowResult.Failed:
				Debug.Log("Erro no add");
				break;
		}

		// MenuPause.pausado = false;
		Time.timeScale = 1f;
	}
}
