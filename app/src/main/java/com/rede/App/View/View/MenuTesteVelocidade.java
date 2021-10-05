package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.DAO.AutenticacaoDAO;
import com.rede.App.View.DAO.SpeedTestDAO;
import com.rede.App.View.JavaBeans.SpeedTest;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.GetSpeedTestHostsHandler;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.App.View.View.test.HttpDownloadTest;
import com.rede.App.View.View.test.HttpUploadTest;
import com.rede.App.View.View.test.PingTest;
import com.rede.ncarede.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * Tela do speed test
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 08/03/2019
 */
public class MenuTesteVelocidade extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static int position = 0;
    static int lastPosition = 0;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;

    SpeedTest speedTest = new SpeedTest();
    SpeedTestDAO speedTestDAO = new SpeedTestDAO();
    Usuario usuario = new Usuario();

    SpeedTestDAO menuTesteVelocidade = new SpeedTestDAO();

    //////////////////////
    private static String geoLong;
    private static String geoLati;
    //////////////////////

    //////////////////////////////////////////////////////////////////////////////
    public static boolean clientePotencial; // Verifica se é nosso cliente ou não
    public static String clientePotencialCPFCNPJ;
    public static String clientePotencialNome;
    //////////////////////////////////////////////////////////////////////////////

    @Override
    public void onResume() {
        super.onResume();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove titulo
        View.inflate(MenuTesteVelocidade.this, R.layout.content_menu_teste_velocidade, null);
        setContentView(R.layout.activity_menu_teste_velocidade);

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;


        final Button startButton = (Button) findViewById(R.id.startButton);
        final DecimalFormat dec = new DecimalFormat("#.###");
        startButton.setText("Iniciar Teste");

        tempBlackList = new HashSet<>();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                startButton.setEnabled(false);

                //Restart test icin eger baglanti koparsa
                if (getSpeedTestHostsHandler == null) {
                    getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                    getSpeedTestHostsHandler.start();
                }

                new Thread(new Runnable() {
                    RotateAnimation rotate;
                    ImageView barImageView = (ImageView) findViewById(R.id.barImageView);
                    TextView pingTextView = (TextView) findViewById(R.id.pingTextView);
                    TextView downloadTextView = (TextView) findViewById(R.id.downloadTextView);
                    TextView uploadTextView = (TextView) findViewById(R.id.uploadTextView);

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startButton.setText("Pingando servidor..."); // Selecting best server based on ping...
                            }
                        });

                        //Get egcodes.speedtest hosts
                        int timeCount = 600; //1min
                        while (!getSpeedTestHostsHandler.isFinished()) {
                            timeCount--;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                            if (timeCount <= 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Sem conexão...", Toast.LENGTH_LONG).show();
                                        startButton.setEnabled(true);
                                        startButton.setTextSize(16);
                                        startButton.setText("Refazer Teste");
                                    }
                                });
                                getSpeedTestHostsHandler = null;
                                return;
                            }
                        }

                        //Find closest server
                        HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                        HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();

                        MenuTesteVelocidade.geoLong = String.valueOf(getSpeedTestHostsHandler.getSelfLat());
                        MenuTesteVelocidade.geoLati = String.valueOf(getSpeedTestHostsHandler.getSelfLon());

                        double selfLat = getSpeedTestHostsHandler.getSelfLat();
                        double selfLon = getSpeedTestHostsHandler.getSelfLon();
                        double tmp = 19349458;
                        double dist = 0.0;
                        int findServerIndex = 0;
                        for (int index : mapKey.keySet()) {
                            if (tempBlackList.contains(mapValue.get(index).get(5))) {
                                continue;
                            }

                            Location source = new Location("Source");
                            source.setLatitude(selfLat);
                            source.setLongitude(selfLon);

                            List<String> ls = mapValue.get(index);
                            Location dest = new Location("Dest");
                            dest.setLatitude(Double.parseDouble(ls.get(0)));
                            dest.setLongitude(Double.parseDouble(ls.get(1)));





                            double distance = source.distanceTo(dest);
                            if (tmp > distance) {
                                tmp = distance;
                                dist = distance;
                                findServerIndex = index;
                            }
                        }

                        String uploadAddr = mapKey.get(findServerIndex);
                        final List<String> info = mapValue.get(findServerIndex);
                        final double distance = dist;

                        if (info == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startButton.setTextSize(12);
                                    startButton.setText("Ocorreu um erro, tente novamente mais tarde.");
                                }
                            });
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startButton.setTextSize(13);
                                //startButton.setText(String.format("Host: %s [Distance: %s km]", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));
                               // startButton.setText(String.format("Host: %s ", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));
                                startButton.setText("Testando...");
                                speedTest.setHost(info.get(2));
                            }
                        });

                        //Init Ping graphic
                        final LinearLayout chartPing = (LinearLayout) findViewById(R.id.chartPing);
                        XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
                        XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                        pingFill.setColor(Color.parseColor("#e5450b"));
                        pingRenderer.addFillOutsideLine(pingFill);
                        pingRenderer.setDisplayChartValues(false);
                        pingRenderer.setShowLegendItem(false);
                        pingRenderer.setColor(Color.parseColor("#4d5a6a"));
                        pingRenderer.setLineWidth(5);
                        final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
                        multiPingRenderer.setXLabels(0);
                        multiPingRenderer.setYLabels(0);
                        multiPingRenderer.setZoomEnabled(false);
                        multiPingRenderer.setXAxisColor(Color.parseColor("#053672"));
                        multiPingRenderer.setYAxisColor(Color.parseColor("#021935"));
                        multiPingRenderer.setPanEnabled(true, true);
                        multiPingRenderer.setZoomButtonsVisible(false);
                        multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                        multiPingRenderer.addSeriesRenderer(pingRenderer);


                        //Init Download graphic
                        final LinearLayout chartDownload = (LinearLayout) findViewById(R.id.chartDownload);
                        XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
                        XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                        downloadFill.setColor(Color.parseColor("#1766c6"));
                        downloadRenderer.addFillOutsideLine(downloadFill);
                        downloadRenderer.setDisplayChartValues(false);
                        downloadRenderer.setColor(Color.parseColor("#0f2138"));
                        downloadRenderer.setShowLegendItem(false);
                        downloadRenderer.setLineWidth(5);
                        final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
                        multiDownloadRenderer.setXLabels(0);
                        multiDownloadRenderer.setYLabels(0);
                        multiDownloadRenderer.setZoomEnabled(false);
                        multiDownloadRenderer.setXAxisColor(Color.parseColor("#2c77d6"));
                        multiDownloadRenderer.setYAxisColor(Color.parseColor("#0f3566"));
                        multiDownloadRenderer.setPanEnabled(false, false);
                        multiDownloadRenderer.setZoomButtonsVisible(false);
                        multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                        multiDownloadRenderer.addSeriesRenderer(downloadRenderer);

                        //Init Upload graphic
                        final LinearLayout chartUpload = (LinearLayout) findViewById(R.id.chartUpload);
                        XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
                        XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                        uploadFill.setColor(Color.parseColor("#4d5a6a"));
                        uploadRenderer.addFillOutsideLine(uploadFill);
                        uploadRenderer.setDisplayChartValues(false);
                        uploadRenderer.setColor(Color.parseColor("#ffffff"));
                        uploadRenderer.setShowLegendItem(false);
                        uploadRenderer.setLineWidth(5);
                        final XYMultipleSeriesRenderer multiUploadRenderer = new XYMultipleSeriesRenderer();
                        multiUploadRenderer.setXLabels(0);
                        multiUploadRenderer.setYLabels(0);
                        multiUploadRenderer.setZoomEnabled(false);
                        multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
                        multiUploadRenderer.setYAxisColor(Color.parseColor("#ffffff"));
                        multiUploadRenderer.setPanEnabled(false, false);
                        multiUploadRenderer.setZoomButtonsVisible(false);
                        multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                        multiUploadRenderer.addSeriesRenderer(uploadRenderer);

                        //Reset value, graphics
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pingTextView.setText("0 ms");
                                chartPing.removeAllViews();
                                downloadTextView.setText("0 Mbps");
                                chartDownload.removeAllViews();
                                uploadTextView.setText("0 Mbps");
                                chartUpload.removeAllViews();
                            }
                        });
                        final List<Double> pingRateList = new ArrayList<>();
                        final List<Double> downloadRateList = new ArrayList<>();
                        final List<Double> uploadRateList = new ArrayList<>();
                        Boolean pingTestStarted = false;
                        Boolean pingTestFinished = false;
                        Boolean downloadTestStarted = false;
                        Boolean downloadTestFinished = false;
                        Boolean uploadTestStarted = false;
                        Boolean uploadTestFinished = false;

                        //Init Test
                        final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 6);
                        final HttpDownloadTest downloadTest = new HttpDownloadTest(uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));
                        final HttpUploadTest uploadTest = new HttpUploadTest(uploadAddr);


                        //Tests
                        while (true) {
                            if (!pingTestStarted) {
                                pingTest.start();
                                pingTestStarted = true;
                            }
                            if (pingTestFinished && !downloadTestStarted) {
                                downloadTest.start();
                                downloadTestStarted = true;
                            }
                            if (downloadTestFinished && !uploadTestStarted) {
                                uploadTest.start();
                                uploadTestStarted = true;
                            }


                            //Ping Test
                            if (pingTestFinished) {
                                //Failure
                                if (pingTest.getAvgRtt() == 0) {
                                    System.out.println("Erro ping...");
                                } else {
                                    //Success
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pingTextView.setText(dec.format(pingTest.getAvgRtt()) + " ms");
                                        }
                                    });
                                }
                            } else {
                                pingRateList.add(pingTest.getInstantRtt());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pingTextView.setText(dec.format(pingTest.getInstantRtt()) + " ms");

                                        speedTest.setPing(dec.format(pingTest.getInstantRtt()).replace(",", "."));
                                    }
                                });

                                //Update chart
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Creating an  XYSeries for Income
                                        XYSeries pingSeries = new XYSeries("");
                                        pingSeries.setTitle("");

                                        int count = 0;
                                        List<Double> tmpLs = new ArrayList<>(pingRateList);
                                        for (Double val : tmpLs) {
                                            pingSeries.add(count++, val);
                                        }

                                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                        dataset.addSeries(pingSeries);

                                        GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiPingRenderer);
                                        chartPing.addView(chartView, 0);

                                    }
                                });
                            }


                            //Download Test
                            if (pingTestFinished) {
                                if (downloadTestFinished) {
                                    //Failure
                                    if (downloadTest.getFinalDownloadRate() == 0) {
                                        System.out.println("Download error...");
                                    } else {
                                        //Success
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                downloadTextView.setText(dec.format(downloadTest.getFinalDownloadRate()) + " Mbps");
                                                speedTest.setDownload(dec.format(downloadTest.getFinalDownloadRate()).replace(",", "."));
                                            }
                                        });
                                    }
                                } else {
                                    //Calc position
                                    double downloadRate = downloadTest.getInstantDownloadRate();
                                    downloadRateList.add(downloadRate);
                                    position = getPositionByRate(downloadRate);

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                            rotate.setInterpolator(new LinearInterpolator());
                                            rotate.setDuration(100);
                                            barImageView.startAnimation(rotate);
                                            downloadTextView.setText(dec.format(downloadTest.getInstantDownloadRate()) + " Mbps");

                                        }

                                    });
                                    lastPosition = position;

                                    //Update chart
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Creating an  XYSeries for Income
                                            XYSeries downloadSeries = new XYSeries("");
                                            downloadSeries.setTitle("");

                                            List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                            int count = 0;
                                            for (Double val : tmpLs) {
                                                downloadSeries.add(count++, val);
                                            }

                                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                            dataset.addSeries(downloadSeries);

                                            GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiDownloadRenderer);
                                            chartDownload.addView(chartView, 0);
                                        }
                                    });

                                }
                            }


                            //Upload Test
                            if (downloadTestFinished) {
                                if (uploadTestFinished) {
                                    //Failure
                                    if (uploadTest.getFinalUploadRate() == 0) {
                                        System.out.println("Upload error...");
                                    } else {
                                        //Success
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                uploadTextView.setText(dec.format(uploadTest.getFinalUploadRate()) + " Mbps");
                                                speedTest.setUpload(dec.format(uploadTest.getFinalUploadRate()).replace(",", "."));
                                            }
                                        });
                                    }
                                } else {
                                    //Calc position
                                    double uploadRate = uploadTest.getInstantUploadRate();
                                    uploadRateList.add(uploadRate);
                                    position = getPositionByRate(uploadRate);

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                            rotate.setInterpolator(new LinearInterpolator());
                                            rotate.setDuration(100);
                                            barImageView.startAnimation(rotate);
                                            uploadTextView.setText(dec.format(uploadTest.getInstantUploadRate()).replace(",", "."));
                                        }

                                    });
                                    lastPosition = position;

                                    //Update chart
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Creating an  XYSeries for Income
                                            XYSeries uploadSeries = new XYSeries("");
                                            uploadSeries.setTitle("");

                                            int count = 0;
                                            List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                            for (Double val : tmpLs) {
                                                if (count == 0) {
                                                    val = 0.0;
                                                }
                                                uploadSeries.add(count++, val);
                                            }

                                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                            dataset.addSeries(uploadSeries);

                                            GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiUploadRenderer);
                                            chartUpload.addView(chartView, 0);
                                        }
                                    });

                                }
                            }

                            // Test bitti
                            if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                                break;
                            }

                            if (pingTest.isFinished()) {
                                pingTestFinished = true;
                            }
                            if (downloadTest.isFinished()) {
                                downloadTestFinished = true;
                            }
                            if (uploadTest.isFinished()) {
                                uploadTestFinished = true;
                            }

                            if (pingTestStarted && !pingTestFinished) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                }
                            } else {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                }
                            }
                        }

                        //Thread bitiminde button yeniden aktif ediliyor
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!clientePotencial) {
                                        speedTest.setNomeCliente(usuario.getNome());
                                        speedTest.setCodCli(usuario.getCpfCnpjAutenticacao());
                                    } else {
                                        speedTest.setNomeCliente(clientePotencialNome);
                                        speedTest.setCodCli(clientePotencialCPFCNPJ);
                                    }


                                    speedTest.setUpload(uploadTextView.getText().toString());


                                    //System.err.println(Ferramentas.retornaSSIDAccessPoint(MenuTesteVelocidade.this).toUpperCase()+" <======== SSID ========> "+ menuTesteVelocidade.retornaSSIDAccessPointBanco().toUpperCase());
                                    //System.err.println(Ferramentas.retornaMACAccessPoint(MenuTesteVelocidade.this).toUpperCase()+" <======== MAC ========> "+ menuTesteVelocidade.retornaMACAccessPointBanco().toUpperCase());
                                    System.err.println(Ferramentas.getIPPublicoAccessPoint(MenuTesteVelocidade.this).toUpperCase()+" <======== IP ========> "+ menuTesteVelocidade.retornaIPAccessPointBanco().toUpperCase());

                                    speedTest.setIP(Ferramentas.getIPPublicoAccessPoint(MenuTesteVelocidade.this).toUpperCase());


                                    // Grava no banco apenas os tests feitos em um roteador específico
                                    /*if (Ferramentas.retornaMACAccessPoint(MenuTesteVelocidade.this).toUpperCase().equals(menuTesteVelocidade.retornaMACAccessPointBanco().toUpperCase())
                                    && Ferramentas.retornaIPPublicoAccessPoint(MenuTesteVelocidade.this).equals(menuTesteVelocidade.retornaIPAccessPointBanco().toUpperCase())
                                    && Ferramentas.retornaSSIDAccessPoint(MenuTesteVelocidade.this).toUpperCase().equals( menuTesteVelocidade.retornaSSIDAccessPointBanco().toUpperCase())) {
                                        speedTestDAO.armazenaResultadoSpeedTestBD(speedTest);
                                    }*/


                                    if (Ferramentas.getIPPublicoAccessPoint(MenuTesteVelocidade.this).equals(menuTesteVelocidade.retornaIPAccessPointBanco().toUpperCase())) {
                                        speedTestDAO.armazenaResultadoSpeedTestBD(speedTest);
                                    }


                                } catch (Exception e) {
                                    System.err.println(e);
                                }

                                startButton.setEnabled(true);
                                startButton.setTextSize(16);
                                startButton.setText("Refazer Teste");
                            }
                        });
                    }
                }).start();

            }



        });





        Button buttonBotaoVoltar = (Button) findViewById(R.id.buttonBotaoVoltar);
        buttonBotaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(new Intent(MenuTesteVelocidade.this, MenuPrincipal.class), 0);
                    finish();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/////////////////////////////////////////// Pertinente NavigationDrawer ///////////////////////////////////////////

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    /*    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
        // disabilita aba lateral esquerda
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }



    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_x, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

//        if (id == R.id.action_sair) {
//             MenusAbaLateralEsquerda.botao3Pontinhos(this);
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_planos) {
            MenusAbaLateralEsquerda.botaoPlanos(MenuTesteVelocidade.this);
        } else if (id == R.id.nav_suporte) {
            MenusAbaLateralEsquerda.botaoSuporte(MenuTesteVelocidade.this);
        } else if (id == R.id.nav_servicos) {
            MenusAbaLateralEsquerda.botaoServicos(MenuTesteVelocidade.this);
        } else if (id == R.id.nav_fale_conosco) {
            MenusAbaLateralEsquerda.botaoFaleConosco(MenuTesteVelocidade.this);
        } else if (id == R.id.nav_sobre) {
            MenusAbaLateralEsquerda.botaoSobre(MenuTesteVelocidade.this);
        } else if (id == R.id.nav_options) {
            MenusAbaLateralEsquerda.botaoOpcoes(MenuTesteVelocidade.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            AutenticacaoDAO autdao = new AutenticacaoDAO();
            Usuario usuario = new Usuario();

            autdao.registraLOGDeslogarUsuario(usuario);

        } catch (Exception e) {

        }
    }
}

