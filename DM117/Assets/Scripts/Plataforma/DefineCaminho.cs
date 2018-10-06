using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class DefineCaminho : MonoBehaviour
{
    public Transform[] pontos;  //cria um vetor para atribuir valores de onde a plataforma vai caminhar

    public IEnumerator<Transform> GetPathEnumerator()
    {
        var direction = 1; //1 vai percorrer em direçao ao final
        var index = 0; //Valor que será retornado como índice do vetor Points
        while (true)
        {
            yield return pontos[index]; //retorna o índice do vetor Points

            if (pontos.Length == 1)
                continue;

            if (index <= 0) //plat no 1º ponto do vetor, assim muda direçao p 1 pra ir ate o final do vetor
                direction = 1;

            else if (index >= pontos.Length - 1) //chegando no final do vetor, direçao muda para que va ate o começo
                direction = -1;

            index = index + direction; //Atualiza o índice que será retornado para o script de movimento
        }
    }
    public void OnDrawGizmos() //gera linhas que liga os pontos, melhor para visualizar o trajeto
    {
        if (pontos == null || pontos.Length < 2)
            return;

        for (int i = 1; i < pontos.Length; i++)
        {
            Gizmos.DrawLine(pontos[i - 1].position, pontos[i].position);
        }
    }
}



