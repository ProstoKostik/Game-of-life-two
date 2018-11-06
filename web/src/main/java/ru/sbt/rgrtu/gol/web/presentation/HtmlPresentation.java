package ru.sbt.rgrtu.gol.web.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.rgrtu.gol.game.BoardService;
import ru.sbt.rgrtu.gol.monitoring.MonitoringService;
import ru.sbt.rgrtu.gol.presentation.Presentation;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

public class HtmlPresentation  extends WebPresentation{

    private static final String RELOAD =
            "<script type=\"text/javascript\">\n" +
                    "window.onload=function(){setTimeout(function() { location.reload(true); }, 500);};" +
                    "</script>\n";

    private final BoardService board;
    private final boolean reload;
    private final MonitoringService monitoring;

    private HttpServletResponse response;

    /**
     * Create new instance.
     *
     * @param board  provider of game data
     * @param reload insert reloading script
     */
    @Autowired
    public HtmlPresentation(BoardService board, boolean reload, MonitoringService monitoring) {
        this.board = board;
        this.reload = reload;
        this.monitoring = monitoring;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void show() {
        String header = String.format(HEADER_TEMPLATE, board.getGeneration());
        StringBuilder rows = new StringBuilder();
        for (BigInteger y = BigInteger.ZERO; !y.equals(board.getSizeY()); y = y.add(BigInteger.ONE)) {
            StringBuilder row = new StringBuilder();
            for (BigInteger x = BigInteger.ZERO; !x.equals(board.getSizeX()); x = x.add(BigInteger.ONE)) {
                row.append(board.getPoint(x, y) ? LIVE_CELL : DEAD_CELL);
            }
            rows.append(String.format(ROW_TEMPLATE, row)).append("\n");
        }
        String board = String.format(BOARD_TEMPLATE, rows);
        String footer = String.format(FOOTER_TEMPLATE, monitoring.elapsed());

        String out = String.format(PAGE_TEMPLATE, (reload ? RELOAD : ""), header, board, footer);
        try {
            response.setContentType("text/html");
            response.getWriter().write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
