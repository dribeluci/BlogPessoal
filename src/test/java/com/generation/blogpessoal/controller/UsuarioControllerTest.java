package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

/*O teste da Camada Controller realizará Requisições (http Request) 
 * e na sequencia o teste analisará se as Respostas das Requisições (http Response) foram as esperadas.
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/*a anotação @TestMethodOrder indica em qual ordem os testes serão
executados. A opção MethodOrderer.OrderAnnotaon.class indica que os testes serão
executados na ordem indicada pela anotação @Order inserida em cada teste. Exemplo:
@Order(1) 🡪 indica que este será o primeiro teste que será executado
 */
public class UsuarioControllerTest {

	// fiz essa injeção abaixo para que as requisições feitas no nosso teste, sejam enviadas
	// para nossa aplicação .
	@Autowired
	private TestRestTemplate testRestTemplate;
	// fiz duas injeções porque preciso do service para testar o controller e do banco de dados
	// de usuários, visto que é ele que fará as requisições necessárias.
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start(){

		usuarioRepository.deleteAll();}
	
	@Test
	@DisplayName ("Cadastrar usuário")
	@Order (1)
	public void criarusuario() {
		// A anotação HttpEntity compõe os status das respostas, ou seja, 100,200,300,400,500
		HttpEntity<Usuario> Requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));

		/* Acima foi criado um objeto da Classe HttpEntity chamado requisicao, recebendo
 		um objeto da Classe Usuario. Nesta etapa, o processo é equivalente ao que o Insonia
		faz em uma requisição do po POST: Transforma os atributos num objeto da Classe
		Usuario, que será enviado no corpo da requisição (Request Body).
		 */
		
		
		ResponseEntity<Usuario> Resposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, Requisicao, Usuario.class);

		/*a Requisição HTTP será enviada através do método exchange() da Classe
		TestRestTemplate e a Resposta da Requisição (Response) será recebida pelo objeto
		resposta do tipo ResponseEnty. Para enviar a requisição, é necessário passar 4
		parâmetros:
		 * A URI: Endereço do endpoint (/usuarios/cadastrar);
		* O Método HTTP: Neste exemplo o método POST;
		* O Objeto HpEnty: Neste exemplo o objeto requisicao, que contém o objeto da
		Classe Usuario;
		* O conteúdo esperado no Corpo da Resposta (Response Body): Neste exemplo
		será do po Usuario (Usuario.class).

		 */
		
		assertEquals(HttpStatus.CREATED, Resposta.getStatusCode());
		// esse método acima verifica se o status é correto, no caso tem que ser 201, visto que é
		// uma postagem.
		
		assertEquals(Requisicao.getBody().getNome(), Resposta.getBody().getNome());
		// Verifica se o Atributo Usuario do Objeto da Classe Usuario retornado no Corpo da Requisição 
		//é igual ao Atributo Usuario do Objeto da Classe Usuario Retornado no Corpo da Resposta
		
		assertEquals(Requisicao.getBody().getUsuario(), Resposta.getBody().getUsuario());
		// realiza a mesma função que o método acima só que utilizando o dado usuário,
		// que no caso é o Email.
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

		HttpEntity<Usuario> Requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

		ResponseEntity<Usuario> Resposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, Requisicao, Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, Resposta.getStatusCode());
		// basicamente todo esse código acima cadastra um novo usuário, isso é feito utilizando
		// o service, depois criamos uma requição utilzando o HttpEntity e por último utilizamos
		// o responseEntity para realizar uma requisição, que no caso é do tipo post e vai tentar
		// cadastrar um usuário que já existe, logo tem que da um retorno 400, caso contrário o código
		// não está funcionando perfeitamente.
	}

	@Test
	@Order(3)
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {

		// aqui está cadastrando um novo usuário.
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "https://i.imgur.com/yDRVeK7.jpg"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), 
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123" , "https://i.imgur.com/yDRVeK7.jpg");
		
		
		// aqui é feito a requisição para atualizar o usuário utilizando o objeto criado, que no caso
		// é o usuarioUpdate.
		HttpEntity<Usuario> Requisicao = new HttpEntity<Usuario>(usuarioUpdate);

		
		ResponseEntity<Usuario> Resposta = testRestTemplate
			.withBasicAuth("root", "root")
			/* como o Blog Pessoal está com o Spring Security habilitado
			com autencação do po Hp Basic, o Objeto testRestTemplate dos endpoints que
			exigem autencação, deverá efetuar o login com um usuário e uma senha válida para
			realizar os testes. Para autencar o usuário e a senha ulizaremos o método
			withBasicAuth(user, password) da Classe TestRestTemplate. Como criamos o usuário
			em memória (root), na Classe BasicSecurityConfig, vamos usá-lo para autencar o
			nosso teste.
			 */
			.exchange("/usuarios/atualizar", HttpMethod.PUT, Requisicao, Usuario.class);

		// aqui estão as respostas esperadas, caso algo de errado tem algo errado que precisa
		// ser corrigido no código.
		assertEquals(HttpStatus.OK, Resposta.getStatusCode());

		assertEquals(Requisicao.getBody().getNome(), Resposta.getBody().getNome());

		
		assertEquals(Requisicao.getBody().getUsuario(), Resposta.getBody().getUsuario());
	}

	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		// esses dois usuarioService está cadastrando 2 novos usuários que devem ser retornados.
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));
		
		ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root", "root") // autenticar o usuário para retornar.
			.exchange("/usuarios/tudo", HttpMethod.GET, null, String.class);

		// resposta esperada.
		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}
	@Test
	@Order (5)
	@DisplayName("Retornar pelo ID")
	
	public void retornarpeloid() {
		Optional<Usuario> existeid = usuarioService.cadastrarUsuario(new Usuario(0L,"Felipe Santos","Felipe@email.com.br","lipe12345",""));
		// agora preciso autenticar para consultar pelo id
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root") // autenticar o usuário para retornar.
				.exchange("/usuarios/" + existeid.get().getId(), HttpMethod.GET, null, String.class);
		// vou configurar a resposta esperada, se der errado é pq tem algo errado no código ou no teste.
		assertEquals(HttpStatus.OK,resposta.getStatusCode());
	}
	
	@Test
	@Order (6)
	@DisplayName("Login")
	public void Login () {
		// vou cadastrar um usuário
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Sabrina Sanches", "felipe@email.com.br", "12345678", "https://i.imgur.com/5M2p5Wb.jpg"));
		// agora vou criar a requisição de login
		HttpEntity<UsuarioLogin> resposta = new HttpEntity<UsuarioLogin>(new UsuarioLogin("felipe@email.com.br", "12345678"));

		// vou usar para gerar a resposta do status e dar o caminho para as requisições
		ResponseEntity<UsuarioLogin> corporesposta = testRestTemplate.exchange("/usuarios/logar", HttpMethod.POST, resposta, UsuarioLogin.class);
		
		// confirma se está tudo correto
		assertEquals(HttpStatus.OK, corporesposta.getStatusCode());

	}
	


	}
	
	

	
	
	
	
	
