package com.rede.App.View.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.ncarede.R;

/**
 * Tela que ilustra o texto sobre a empresa
 *
 * @author Igor Maximo
 * @date 05/03/2021
 */
public class ScrollingSobre extends AppCompatActivity {

    WebView textviewSobre;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;

        // Animação de entrada
        Animatoo.animateSwipeLeft(this);



        FloatingActionButton botaoVoltar = (FloatingActionButton) findViewById(R.id.fab);
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
                } catch (Exception e) {

                }
                Intent i = new Intent(ScrollingSobre.this, MenuPrincipal.class);
                startActivity(i);
            }
        });

        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
                } catch (Exception e) {

                }
                Intent i = new Intent(ScrollingSobre.this, MenuPrincipal.class);
                startActivity(i);
                finish();
            }
        });

        textviewSobre = (WebView) findViewById(R.id.textviewSobre);
        String text =
        "<html>" +
        "<body>" +
        "<br>" +
        "<p align=\"justify\">    Há mais de duas décadas, a Rede Telecom conecta você com o que realmente importa. Com sede na cidade de Andradina (SP), a operadora oferece serviços de acesso à internet para os moradores da cidade e também para toda a região. Atualmente, a Rede também está presente nas cidades de Castilho, Ilha Solteira, Itapura, Lavínia, Mirandopólis, Nova Independência, Pereira Barreto e Valparaíso.</p>" +
        "<p align=\"justify\">    E estar próximo dos seus clientes é o que torna a Rede Telecom diferente das demais operadoras. E você sabe por quê? A resposta é muita simples: a proximidade com os clientes torna o atendimento prestado pela Rede muito mais humanizado.</p>" +
        "<p align=\"justify\">    Com atuação na área de telecomunicações, a Rede Telecom conta com um time de colaboradores que acreditam que a conexão, muito mais que um acesso à internet, é um laço capaz de ligar, ainda mais, as pessoas. Justamente por isso, oferece pacotes flexíveis que atendem às necessidades de vários perfis de clientes. Também disponibiliza um suporte técnico muito mais presente.</p>" +
        "<p align=\"justify\">    Provedores de internet regionais, como a Rede Telecom, mantêm proximidade com a realidade local, o que permite uma prática de preços justos, já que os serviços são formatados para atender a real necessidade daquela cidade. Além disso, também é importante destacar que existe muito mais transparência em relação aos serviços prestados. A Rede Telecom se dedicada para que os clientes tenham grande satisfação e se sintam muito confortáveis com nosso apoio e cuidado. Justamente por isso, se tornou referência na área de telecomunicações, atendimento e fornecimento do acesso à internet, proporcionando uma conexão de qualidade que traz acesso à informação, tecnologia e diversão. Tudo isso através do mundo digital. </p>" +
        "<p></p>" +
        "<p></p>" +
        "<hr></hr>" +
        "<p align=\"right\">" +
        "<p align=\"right\" style=\"font-weight: bold; font-size: 12px;\">Desenvolvimento: igor.maximo@redetelecom.com.br</p>" +
        "<p align=\"right\" style=\"font-weight: bold; font-size: 12px;\">Suporte: desenvolvimento@redetelecom.com.br</p>" +
        "</p>" +
        "</body></html>";

        textviewSobre.loadData(text, "text/html", "utf-8");


    }
}
