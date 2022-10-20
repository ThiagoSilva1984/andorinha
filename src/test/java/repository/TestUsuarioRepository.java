package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;
import model.seletor.UsuarioSeletor;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {

	private static final int ID_USUARIO_CONSULTA = 5;

	@EJB
	private UsuarioRepository usuarioRepository;

	@Before
	public void setup() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	// Inserir um novo usuário
	@Test
	public void testa_se_usuario_foi_inserido() {
		Usuario user = new Usuario();
		user.setNome("Usuario do teste de Unidade");
		this.usuarioRepository.inserir(user);

		Usuario inserido = this.usuarioRepository.consultar(user.getId());

		assertThat(user.getId()).isGreaterThan(0);
		assertThat(inserido).isNotNull();
		assertThat(inserido.getNome()).isEqualTo(user.getNome());
		assertThat(inserido.getId()).isEqualTo(user.getId());

		System.out.println(inserido);
	}

	// Consultar determinado usuário
	@Test
	public void testa_consultar_usuario() {
		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA); // id = 5
		assertThat(usuario).isNotNull();
		assertThat(usuario.getId()).isEqualTo(ID_USUARIO_CONSULTA);
		assertThat(usuario.getNome()).isEqualTo("Usuário 5");

		System.out.println(usuario);
	}

	// Alterar os dados de um usuário
	@Test
	public void testa_alterar_usuario() {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		user.setNome("Usuario Atualizado");

		this.usuarioRepository.atualizar(user);

		Usuario userAlterado = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);

		assertThat(userAlterado).isNotNull();
		assertThat(userAlterado.getId()).isEqualTo(ID_USUARIO_CONSULTA);
		assertThat(userAlterado).isEqualToComparingFieldByField(userAlterado);

		System.out.println(userAlterado);
	}

	// Remove os dado de um determinado usuário
	@Test
	public void testa_remover_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		assertThat(user).isNotNull();

		this.usuarioRepository.remover(ID_USUARIO_CONSULTA);

		Usuario usuarioRemovido = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		assertThat(usuarioRemovido).isNull();

		System.out.println(usuarioRemovido);
	}
	
	//Traz uma lista de todos os usuários
	@Test
	public void testa_listar_todos_os_usuarios() {
		List<Usuario> usuarios = this.usuarioRepository.listarTodos();
		
		assertThat(usuarios).isNotNull()
				.isNotEmpty()
				.hasSize(10)
				.extracting("nome")
				.containsExactlyInAnyOrder("Usuário 1", "Usuário 2",
											"Usuário 3", "Usuário 4", 
											"Usuário 5", "João", 
											"José", "Maria", "Ana", "Joselito");
		
		usuarios.forEach(u -> System.out.println(u));
	}

	// Traz uma lista de usuários pelo nome ou por parte do nome
	@Test
	public void testa_pesquisar_usuario_por_nome() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("Jo");

		List<Usuario> usuarios = this.usuarioRepository.pesquisar(seletor);

		assertThat(usuarios).isNotNull().isNotEmpty().hasSize(3).extracting("nome").containsExactlyInAnyOrder("João",
				"José", "Joselito");

		usuarios.forEach(u -> System.out.println(u));
	}

	// Pesquisa um usuário por determinado id
	@Test
	public void testa_pesquisar_usuarios_por_id() {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setId(3);
		List<Usuario> usuarios = this.usuarioRepository.pesquisar(seletor);

		assertThat(usuarios).isNotNull().isNotEmpty().hasSize(1).extracting("nome").containsExactly("Usuário 3");

		usuarios.forEach(u -> System.out.println(u));
	}

	// Conta o número total de usuários
	@Test
	public void testa_contar_numero_de_usuarios() {
		UsuarioSeletor seletor = new UsuarioSeletor();
		Long total = this.usuarioRepository.contar(seletor);

		assertThat(total).isNotNull().isEqualTo(10L);

		System.out.println(total);
	}

	// Conta o número total de usuários com determinado nome
	@Test
	public void testa_contar_numero_de_usuarios_com_determinado_nome() {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("Usuário");
		Long total = this.usuarioRepository.contar(seletor);

		assertThat(total).isNotNull().isEqualTo(5L);

		System.out.println(total);
	}

	// Conta o número total de usuários com determinado nome e id
	@Test
	public void testa_contar_numero_de_usuarios_com_determinado_id() {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setId(1);
		seletor.setId(1);
		Long total = this.usuarioRepository.contar(seletor);

		assertThat(total).isNotNull().isEqualTo(1L);

		System.out.println(total);
	}

	// Conta o número total de usuários com determinado nome e id
	@Test
	public void testa_contar_numero_de_usuarios_com_determinado_nome_e_id() {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("Usuário");
		seletor.setId(1);
		Long total = this.usuarioRepository.contar(seletor);

		assertThat(total).isNotNull().isEqualTo(1);

		System.out.println(total);
	}

}
