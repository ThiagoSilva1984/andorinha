package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;

public abstract class AbstractCrudRepository {

	@PersistenceContext
	protected EntityManager em;

	// criando o DS atraves de anotação
	@Resource(name = "andorinhaDS") // essa anotação aqui é uma injeção de dependecia
	protected DataSource ds;

	protected int recuperaProximoValorDaSequence(String nomeSequence)throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = ds.getConnection()) {

			PreparedStatement ps = c.prepareStatement("select nextval(?)");
			ps.setString(1, nomeSequence);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}

			throw new ErroAoConsultarBaseException("Erro ao recuperar próximo valor da sequence" + nomeSequence, null);

		} catch (SQLException e) {
			throw new ErroAoconectarNaBaseException("Ocorreu um erro ao acessar a base de dados", e);
		}
	}
}
