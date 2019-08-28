package br.ufrn.eaj.tads.exemploasynctask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.IOException
import java.net.URL
import android.R.attr.bitmap
import android.os.AsyncTask
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.os.AsyncTask.execute




class MainActivity : AppCompatActivity() {

    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloadImagem()
    }

    private fun downloadImagem() {
        val tarefa = BaixarArquivosTask()
        tarefa.execute("http://tads.eaj.ufrn.br/projects/tads.png")
    }

    private inner class BaixarArquivosTask : AsyncTask<String, Int, Bitmap>() {

        /*
        Método executado antes da Thread inicia, sendo uma boa oportunidade para exibir
        uma janela de progresso para o usuário ou uma mensagem do tipo "por favor, aguarde"
        Esse método executa na UI Thread
         */
        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.setVisibility(View.VISIBLE)

        }

        /*
        * Método executado em background por uma thread. Aqui deve estar todo o processamento pesado
        * Esse método pode retornar qualquer objeto. O tipo do objeto é espeficiado no 3º parámetro
        * da classe genérica AsyncTest <1, 2, 3>
        * O dado de retorno será passado como parâmetro para o método onPostExecute()
         */
        override fun doInBackground(vararg strings: String): Bitmap? {
            try {
                bitmap = loadImageFromNetwork(strings[0])
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return bitmap
        }

        /*
        * Método chamado na UI thread e recebe geralmente um inteiro para informar a quantidade do progresso
        * O progresso deve ser informado em background dentro do método doInBackGround(). Para isso,
        * dentro do doInBackgroud é necessário chamar o método publishProgress(int)
         */
        //protected override fun onProgressUpdate(vararg progress: Int) {

       // }

        /*
        * Método executado na Ui Thread em que podemos atualizar uma view com o resultado do processamento
        * realizado em background. Ele é implementado internamente usando o handler.
         */
        override fun onPostExecute(result: Bitmap) {
            imageView.setImageBitmap(result)
            progressBar.setVisibility(View.INVISIBLE)

        }
    }

    @Throws(IOException::class)
    fun loadImageFromNetwork(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        val img = URL(url).openStream()
        // Converte a InputStream do Java para Bitmap
        bitmap = BitmapFactory.decodeStream(img)
        img.close()
        return bitmap
    }
}
