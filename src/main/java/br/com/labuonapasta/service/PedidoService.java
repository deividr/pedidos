package br.com.labuonapasta.service;

import br.com.labuonapasta.banco.PedidoDao;
import br.com.labuonapasta.excessao.NegocioException;
import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.ItemPedido;
import br.com.labuonapasta.modelo.Pedido;
import br.com.labuonapasta.util.Transactional;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Operacoes gerais com Pedido
 */
public class PedidoService implements Serializable {

	private static final long serialVersionUID = 958191867021609952L;

	private final PedidoDao pedidoDao;

	@Inject
	public PedidoService(PedidoDao pedidoDao) {
		this.pedidoDao = pedidoDao;
	}

	/**
	 * Incluir novo pedido.
	 *
	 * @param novoPedido     Objeto Pedido que sera incluído.
	 * @param gerarImpressao Indica se é para gerar ocorrencia para serviço de
	 *                       impressao gerar o cupom.
	 * @return Retorna o pedido com o a informação de ID preenchido.
	 * @throws RegistroExistenteException quando o pedido já existir na base.
	 */
	@Transactional
	public Pedido inserir(Pedido novoPedido, boolean gerarImpressao) {
		if (novoPedido.getItens().isEmpty()) {
			throw new NegocioException("Pedido deve possuir pelo menos um item cadastrado.");
		}

		// Calcular quantos dias serão subtraídos para obter o primeiro dia da semana
		// (Segunda-Feira).
		LocalDate dataRetirada = novoPedido.getDataRetirada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int dias = dataRetirada.getDayOfWeek().getValue() - 1;

		LocalDate primeiroDia = dataRetirada.minusDays(dias);

		LocalDate ultimoDia = primeiroDia.plusDays(6);

		try {
			int i = pedidoDao.obterUltimoNumeroPedido(Date.from(primeiroDia.atStartOfDay(ZoneId.systemDefault()).toInstant()),
					Date.from(ultimoDia.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			// Somar 1 ao último número de pedido cadastrado.
			novoPedido.setNumeroPedido(i + 1);
		} catch (NoResultException ex) {
			// Se não existir nenhum pedido, iniciar o número a partir de 500.
			novoPedido.setNumeroPedido(500);
		}

		Pedido pedido = pedidoDao.create(novoPedido);

		return pedido;
	}

	/**
	 * Efetuar alterações no pedido.
	 *
	 * @param pedido         com as informações para serem alteradas.
	 * @param gerarImpressao Indica se é para gerar ocorrência para serviço de
	 *                       impressão gerar o cupom.
	 */
	@Transactional
	public void alterar(Pedido pedido, boolean gerarImpressao) {
		if (pedido.getItens().isEmpty()) {
			throw new NegocioException("Pedido deve possuir pelo menos um item cadastrado.");
		}

		pedidoDao.update(pedido);
	}

	@Transactional
	public void alterarSoPedido(Pedido pedido) {
		pedidoDao.update(pedido);
	}

	/**
	 * Excluir o pedido definitivamente.
	 *
	 * @param pedido Que será excluido
	 * @throws NoResultException quando o pedido não existir mais na base.
	 */
	@Transactional
	public void excluir(Pedido pedido) throws NoResultException {
		pedido = pedidoDao.getById(pedido.getPedId());

		if (!Objects.isNull(pedido)) {
			pedidoDao.delete(pedido);
			pedidoDao.getEntityManager().flush();
		} else {
			throw new NoResultException("Pedido não está mais disponível para exclusão");
		}
	}

	public void emitirRelatorio(Object document, List<Pedido> pedidos) {
		Workbook wb = (HSSFWorkbook) document;

		// Remover a primeira sheet incluída automáticamente pelo PrimeFaces
		wb.removeSheetAt(0);

		Sheet sheet = wb.createSheet("Relação de Pedidos");
		int intRow = 0;
		Row cabecalho = sheet.createRow(intRow);

		// Estilizar a celula
		CellStyle styleCabecalho = wb.createCellStyle();

		styleCabecalho.setAlignment(HorizontalAlignment.CENTER);
		styleCabecalho.setVerticalAlignment(VerticalAlignment.CENTER);

		styleCabecalho.setBorderBottom(BorderStyle.THICK);
		styleCabecalho.setBorderLeft(BorderStyle.THICK);
		styleCabecalho.setBorderRight(BorderStyle.THICK);
		styleCabecalho.setBorderTop(BorderStyle.THICK);

		// styleCabecalho.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
		styleCabecalho.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		styleCabecalho.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Estilizar a fonte
		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 11);
		styleCabecalho.setFont(font);

		Cell cell0 = cabecalho.createCell(0);
		Cell cell1 = cabecalho.createCell(1);
		Cell cell2 = cabecalho.createCell(2);
		Cell cell3 = cabecalho.createCell(3);
		Cell cell4 = cabecalho.createCell(4);

		cell0.setCellValue("Num.");
		cell0.setCellStyle(styleCabecalho);

		cell1.setCellValue("Cliente");
		cell1.setCellStyle(styleCabecalho);

		cell2.setCellValue("Telefone");
		cell2.setCellStyle(styleCabecalho);

		cell3.setCellValue("Geladeira");
		cell3.setCellStyle(styleCabecalho);

		cell4.setCellValue("Produtos");
		cell4.setCellStyle(styleCabecalho);

		for (Pedido pedido : pedidos) {
			intRow++;
			Row detalhe = sheet.createRow(intRow);
			detalhe.createCell(0).setCellValue(pedido.getNumeroPedido());
			detalhe.createCell(1).setCellValue(pedido.getCliente().getNome());
			detalhe.createCell(2).setCellValue(pedido.getCliente().getTelefone1());
			detalhe.createCell(3).setCellValue(pedido.getGeladeira());

			StringBuilder produtos = new StringBuilder();

			for (int i = 0; i < pedido.getItens().size(); i++) {
				if (i > 0) {
					produtos.append("\n");
				}
				ItemPedido item = pedido.getItens().get(i);
				produtos.append(item.getQtde().toString());
				produtos.append(" ");
				produtos.append(item.getProduto().getUnidade().getCodigo().toLowerCase());
				produtos.append("  ");
				produtos.append(pedido.getItens().get(i).getProduto().getNome());
			}

			CellStyle produtosStyle = wb.createCellStyle();
			produtosStyle.setWrapText(true);
			Cell cellProduto = detalhe.createCell(4);
			cellProduto.setCellValue(produtos.toString());
			cellProduto.setCellStyle(produtosStyle);
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
	}
}
