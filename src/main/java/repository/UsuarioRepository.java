package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;

@Stateless //estou transformando o meu usuario repository em um EJB do tipo Stateless (um EJB sem estado)
public class UsuarioRepository extends AbstractCrudRepository {

	public void inserir(Usuario usuario) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			int id = this.recuperaProximoValorDaSequence("seq_usuario");
			usuario.setId(id);
			
			// método para inserir usuario na base de dados
			// 1º cria a query
			PreparedStatement ps = c.prepareStatement("insert into usuario (id, nome) values (?, ?)");
			// 2º vou inserir na base de dados setando os valores
			ps.setInt(1, usuario.getId()); // o id foi inserido logo acima no objeto que vem por parametro (usuario)
			ps.setString(2, usuario.getNome()); // o nome ja está vindo no objeto usuario
			
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir usuario", e);
		}
	}

	public void atualizar(Usuario usuario) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			PreparedStatement ps = c.prepareStatement("UPDATE usuario SET nome = ?  WHERE id = ?");
			ps.setString(1, usuario.getNome());
			ps.setInt(2, usuario.getId());
			ps.execute();
			ps.close();
			
		} catch (

		SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir usuario", e);
		}
	}

	public void remover(int id) throws ErroAoConsultarBaseException, ErroAoconectarNaBaseException {
		try (Connection c = super.ds.getConnection()) {
			PreparedStatement ps = c.prepareStatement("DELETE FROM usuario WHERE id = ?");
			ps.setInt(1, id);		
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao deletar usuario", e);
		}
	}

	public Usuario consultar(int id) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {

		try (Connection c = super.ds.getConnection()) {
			Usuario user = null;

			// vou fazer um select para consultar
			PreparedStatement ps = c.prepareStatement("select id, nome from usuario where id = ? ");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // se veio o resultado
				user = new Usuario();
				user.setId(rs.getInt("id"));
				user.setNome(rs.getString("nome"));
			}
			rs.close();
			ps.close();
			return user;
		} catch (

		SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar usuario", e);
		}
	}

	public List<Usuario> listarTodos() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {

			List<Usuario> usuarios = new ArrayList<Usuario>();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM usuario");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuarios.add(usuario);
			}
			rs.close();
			ps.close();

			return usuarios;

		} catch (

		SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar usuario", e);
		}
	}

}
