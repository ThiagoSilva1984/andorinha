package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {
	
	private static final int ID_USUARIO_CONSULTA = 5;
	private static final int ID_USUARIO_SEM_TWEET = 1;
	
	@EJB
	private UsuarioRepository usuarioRepository;

	@Before 
	public void setup() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	public void testa_se_usuario_foi_inserido() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = new Usuario();
		user.setNome("Usuario do teste de Unidade");
		this.usuarioRepository.inserir(user);
		
		Usuario inserido = this.usuarioRepository.consultar(user.getId());
		
		assertThat( user.getId() ).isGreaterThan(0);	
		assertThat( inserido ).isNotNull();
		assertThat( inserido.getNome() ).isEqualTo(user.getNome());
		assertThat( inserido.getId() ).isEqualTo(user.getId());
	}

	@Test
	public void testa_consultar_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		assertThat( user ).isNotNull();
		assertThat( user.getId() ).isEqualTo(ID_USUARIO_CONSULTA);
		assertThat( user.getNome() ).isEqualTo("Usuário 5");
		System.out.println(user.getId() + " - " + user.getNome());
	}
	
	@Test
	public void testa_atualizar_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);	
		user.setNome("Usuário 1");
		this.usuarioRepository.atualizar(user);	
		user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);	

		assertThat( user ).isNotNull();
		assertThat( user.getId() ).isEqualTo(ID_USUARIO_CONSULTA);	
	}
	
	@Test
	public void testa_remover_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		assertThat( user ).isNotNull();
		
		this.usuarioRepository.remover(ID_USUARIO_CONSULTA);
		
		Usuario usuarioRemovido = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);	
		assertThat( usuarioRemovido ).isNull();			
	}
	
	@Test
	public void testa_listar_usuarios() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		usuarios = this.usuarioRepository.listarTodos();
		
		assertThat( usuarios ).isNotNull();
	}
	

}
