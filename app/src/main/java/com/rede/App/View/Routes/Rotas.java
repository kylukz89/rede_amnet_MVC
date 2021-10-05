package com.rede.App.View.Routes;

import com.rede.App.View.ToolBox.VariaveisGlobais;

/**
 * Todos arquivos PHP do servidor
 *
 * OBS: Endpoints de APIs
 *
 * @author Igor Maximo
 * @date 19/02/2019
 */
public class Rotas {

    ////////////////////////////////////////////////////////////
    //                           MAIN                        //
    ////////////////////////////////////////////////////////////
    public static String ROTA_PADRAO = VariaveisGlobais.IP + "Rotas.php?"; // Arquivo de todas as rotas.
    ////////////////////////////////////////////////////////////
    //                      SELECTS ENDPOINT                  //
    ////////////////////////////////////////////////////////////
    public static String SELECT_VERSIONAMENTO = "func=selectVersionamentoEStatus" + VariaveisGlobais.AUT_GET; // Para controle de versionamento
    public static String SELECT_AUTENTICACAO = "func=selectAutenticacaoCentralAssinante" + VariaveisGlobais.AUT_GET; // Para autenticação do usuário no app
    public static String SELECT_PLANOS_CLIENTE_ROLETA = "func=selectPlanosClientePorCodCliRoleta" + VariaveisGlobais.AUT_GET; // Para carregar os dados básicos do plano para exibir posteriormente na ROLETA DE PLANOS MENU PRINCIPAL
    public static String SELECT_DADOS_PLANO_CLIENTE_CODSERCLI = "func=selectPlanoClientePorCodSerCli" + VariaveisGlobais.AUT_GET; // Posteriormente filtrar por codsercli
    public static String SELECT_DADOS_PLANOS_VENDAS_ONLINE = "func=selectTodosPlanosVenda" + VariaveisGlobais.AUT_GET; // Posteriormente filtrar por codsercli
    public static String SELECT_DADOS_CLIENTE = "func=selectDadosClientePorCPFCNPJ" + VariaveisGlobais.AUT_GET; // Para carregar a classe usuario com todos os dados do CLIENTE
    public static String SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA = "func=selectFaturasClientePorCodCliSegundaVia" + VariaveisGlobais.AUT_GET; // Seleciona todas as faturas do cliente de DATA até DATA
    public static String SELECT_HISTORICO_PAGAMENTOS = "func=selectHistoricoPagamentos" + VariaveisGlobais.AUT_GET; // Seleciona todas as faturas do cliente de DATA até DATA
    public static String SELECT_PLANOS_SUSPENSOS_DEBITO_HABILITACAO_PROVISORIA = "func=selectPlanosSuspensosClientePorCodCli" + VariaveisGlobais.AUT_GET; // Retornar todos os planos suspensos por débito
    public static String SELECT_EXTRATO_FINANCEIRO = "func=selectExtratoFinanceiroClientePorCodCli" + VariaveisGlobais.AUT_GET; // Para retornar todos os extratos financeiros do cliente
    public static String SELECT_NOTAS_FISCAIS = "func=selectNotasFiscaisCodCli" + VariaveisGlobais.AUT_GET; // Para retornar todas as notas fiscais do cliente
    public static String SELECT_LINK_BOLETO_FATURA_MENSALIDADE = "func=selectLinkBoletoTitulo" + VariaveisGlobais.AUT_GET; // Para retornar parte do link do boleto retornado da API
    public static String SELECT_EXTRATO_CONEXAO_PPPOE = "func=selectExtratoConexaoPorPPPoE" + VariaveisGlobais.AUT_GET; // Para consultar os extratos de conexão por plano
    public static String SELECT_SE_FOI_GERADO_BOLETO_ANTERIORMENTE = "func=selectSeFoiGeradoBoletoAnteriormente" + VariaveisGlobais.AUT_GET; // Para verificar se boleto foi gerado anteriormente e exibir um alerta ao usuário
    public static String SELECT_SE_APP_HABILITADO_PAGAMENTO_POR_CREDITO = "func=selectSeAppBloqueadoPagamentoCredito" + VariaveisGlobais.AUT_GET; // Para verificar se o app está bloqueado
    public static String SELECT_SE_APP_HABILITADO_PAGAMENTO_POR_DEBITO = "func=selectSeAppBloqueadoPagamentoDebito" + VariaveisGlobais.AUT_GET; // Para verificar se o app está bloqueado
    public static String SELECT_SE_APP_HABILITADO_EMISSAO_BOLETOS = "func=selectSeAppBloqueadoEmissaoBoletos" + VariaveisGlobais.AUT_GET; // Para verificar se o app está bloqueado
    public static String SELECT_SE_APP_HABILITADO_CODIGO_BARRAS = "func=selectSeAppBloqueadoCodigoBarras" + VariaveisGlobais.AUT_GET; // Para verificar se o app está bloqueado
    public static String SELECT_SE_APP_HABILITADO_MUDANCA_PLANO = "func=selectSeAppHabilitadoMudancaPlano" + VariaveisGlobais.AUT_GET; // Para verificar se o app está bloqueado
    public static String SELECT_SE_CODSER_DO_PLANO_CODSERCLI_EM_QUESTAO_E_FIBRA = "func=selectSeCodSerdoPlanoCodSerCliAtualEFibra" + VariaveisGlobais.AUT_GET; // Para verificar se o app está bloqueado
    public static String SELECT_QTS_DIAS_PODE_GERAR_BOLETO_VENCIDO = "func=selectQtdsDiasPodeGerarBoletoVencido" + VariaveisGlobais.AUT_GET; // Consulta para ver com quantos dias pode gerar um boleto vencido (PADRÃO 30 DIAS)
    public static String SELECT_TODAS_NOTIFICACOES_USUARIO = "func=selectTodasNotificacoesUsuario" + VariaveisGlobais.AUT_GET; // Consulta todas as notificações do usuário
    public static String SELECT_SE_CLIENTE_EXISTE_BANCO_CLIENTEPOTENCIAL = "func=selectSeCPFExisteClientePotencial" + VariaveisGlobais.AUT_GET; // Consulta todas as notificações do usuário
    public static String SELECT_DADOS_CLIENTEPOTENCIAL = "func=selectDadosClientePotencial" + VariaveisGlobais.AUT_GET; // Consulta todas as notificações do usuário
    public static String SELECT_MAC_ROTEADOR_BANCO = "func=selectMACdoRoteadorBanco" + VariaveisGlobais.AUT_GET; // Consulta todas as notificações do usuário
    public static String SELECT_IP_ROTEADOR_BANCO = "func=selectIPdoRoteadorBanco" + VariaveisGlobais.AUT_GET; // Consulta todas as notificações do usuário
    public static String SELECT_SSID_ROTEADOR_BANCO = "func=selectSSIDdoRoteadorBanco" + VariaveisGlobais.AUT_GET; // Consulta todas as notificações do usuário
    public static String SELECT_BOTAO_SPEEDTEST = "func=selectSeBotaoSpeedTestAtivado" + VariaveisGlobais.AUT_GET; // Consulta se o botão de test de velocidade deve estar ativo
    public static String SELECT_BOTAO_ATENDIMENTO = "func=selectSeBotaoAtendimentoAtivado" + VariaveisGlobais.AUT_GET; // Consulta se o botão de test de velocidade deve estar ativo
    public static String SELECT_BOTAO_DEBITO_AUTOMATICO = "func=selectSeBotaoDebitoAutomaticoAtivado" + VariaveisGlobais.AUT_GET; // Consulta se o botão de test de velocidade deve estar ativo
    public static String SELECT_BOTAO_IPTV = "func=selectSeBotaoIPTVAtivado" + VariaveisGlobais.AUT_GET; // Consulta se o botão de test de velocidade deve estar ativo
    public static String SELECT_BOTAO_CODIGO_BARRAS = "func=selectCodBarras" + VariaveisGlobais.AUT_GET; // Coleta o código de barras da fatura
    public static String SELECT_VENCIMENTOS = "func=selectVencimentos" + VariaveisGlobais.AUT_GET; // Coleta o código de barras da fatura
    public static String SELECT_VENCIMENTOS_POR_CODCOB = "func=selectVencimentosPorFormaCobranca" + VariaveisGlobais.AUT_GET; // Coleta o código de barras da fatura
    public static String SELECT_SE_FORMA_COBRANCA_PLANO_SUPORTA_UPGRADE = "func=selectSeFormaCobrancaPlanoSuportaUpgrade" + VariaveisGlobais.AUT_GET; // Coleta o código de barras da fatura
    public static String SELECT_DADOS_TRANSACAO_DEBITO = "func=selectStatusTransacaoDebito" + VariaveisGlobais.AUT_GET; // Coleta o código de barras da fatura
    public static String SELECT_SE_CATEGORIA_PLANO_ATUAL_EMPRESARIAL = "func=selectTipoCategoriaPlanoAtual" + VariaveisGlobais.AUT_GET; // Coleta o código de barras da fatura
    public static String SELECT_SE_PLANO_POSSUI_DESCONTOS = "func=selectSePlanoPossuiDescontosOuAcrescimos" + VariaveisGlobais.AUT_GET; // Coleta o código de barras da fatura
    public static String SELECT_LISTA_OPCOES_ATENDIMENTO = "func=selectListaOpcoesAtendimento" + VariaveisGlobais.AUT_GET;
    public static String SELECT_LISTA_OPCOES_ATENDIMENTO_CATEGORIA_ITEM_RESPOSTA = "func=selectListaOpcoesRespostaCategoriaAtendimento" + VariaveisGlobais.AUT_GET;
    ///////////////////////////////////// COMANDOS ////////////////////////////////
    public static String EXECUTE_HABILITACAO_PROVISORIA = "func=executeHabProv" + VariaveisGlobais.AUT_GET; // Para habilitação provisória
    public static String EXECUTE_ENVIA_SENHA_EMAIL = "func=enviaEmailSenhaCentralAssinante" + VariaveisGlobais.AUT_GET; // Para enviar senha da central para o e-mail
    public static String EXECUTE_UPGRADE_PLANO_CLIENTE = "func=setUpgradePlanoCliente" + VariaveisGlobais.AUT_GET; // Para enviar senha da central para o e-mail
    //////////////////////////////// OPERAÇÕES FINANCEIRAS ////////////////////////
    public static String PAY_MENSALIDADE_SEGVIA = "func=payUmaFaturaMensalidadeCreditoDebito" + VariaveisGlobais.AUT_GET; // Para pagar uma mensalidade
    public static String PAY_CARTAO_VINDI_CLIENTE = "func=setCartaoClienteVindi" + VariaveisGlobais.AUT_GET; // Para pagar uma mensalidade
    ////////////////////////////////////////////////////////////
    //                       INSERT ENDPOINT                  //
    ////////////////////////////////////////////////////////////
    public static String INSERT_DADOS_SPEED_TEST = "func=insertDadosSpeedTestBanco" + VariaveisGlobais.AUT_GET; // Grava no banco o speed test
    public static String INSERT_DADOS_CADASTRO_POTENCIAL_CLIENTE = "func=insertCadastraDadosClienteEmPotencial" + VariaveisGlobais.AUT_GET; // Cadastra potencial cliente
    public static String INSERT_ERRO_LOG = "func=insertErroLOGApps" + VariaveisGlobais.AUT_GET; // Grava erro do app no log do servidor
    public static String INSERT_SOLICITAR_ANTIVIRUS_CHAVE = "func=setSolicitarAtivacaoAntivirus" + VariaveisGlobais.AUT_GET;
    public static String INSERT_ABRIR_ATENDIMENTO = "func=setAbrirAtendimentoProblemaOuSolicitacao" + VariaveisGlobais.AUT_GET;
    ////////////////////////////////////////////////////////////
    //                       UPDATE ENDPOINT                  //
    ////////////////////////////////////////////////////////////
    public static String UPDATE_DADOS_CONTATO = "func=updateDadosContatoSimetra" + VariaveisGlobais.AUT_GET; // Para registrar a deslogada do cliente
    public static String UPDATE_ALTERA_SENHA = "func=updateSenhaCentralAssinante" + VariaveisGlobais.AUT_GET; // Para alterar a senha
    public static String UPDATE_DESLOGA_USUARIO = "func=registraLOGDeslogarUsuario" + VariaveisGlobais.AUT_GET; // Para registrar a deslogada do cliente
    public static String UPDATE_TOKEN_FIREBASE = "func=updateTokenFirebase" + VariaveisGlobais.AUT_GET; // Para registrar a deslogada do cliente
    public static String UPDATE_MARCA_NOTIFICACOES_LIDAS = "func=marcaComoLidaNotificacoes" + VariaveisGlobais.AUT_GET; // Para registrar a deslogada do cliente
    public static String UPDATE_MARCA_MARCA_SIM_NAO = "func=marcaSimOuNaoNotificacao" + VariaveisGlobais.AUT_GET; // Para registrar a deslogada do cliente
    public static String UPDATE_GRAVA_AVALIACAO_VISITA_TECNICA = "func=setAvaliacaoEstrelasServicoTecnico" + VariaveisGlobais.AUT_GET; // Para registrar a deslogada do cliente
    ///////////////////////////////////// REDIRECIONAMENTOS ////////////////////////////////
    public static String REDIRECT_SITE_VENDAS = "func=redirectSiteVendas" + VariaveisGlobais.AUT_GET; // Para redirecionar para a plataforma de vendas
}