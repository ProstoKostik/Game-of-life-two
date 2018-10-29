package ru.sbt.rgrtu.gol.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.rgrtu.gol.game.BoardService;
import ru.sbt.rgrtu.gol.monitoring.MonitoringService;
import ru.sbt.rgrtu.gol.presentation.Presentation;

import javax.servlet.http.HttpServletResponse;

public class HtmlPresentation implements Presentation {

    private static final String PAGE_TEMPLATE =
    "<html>\n" +
        "<head>\n" +
            "<style>\n" +
                "body {background-color: black; color:gray}\n" +
                "table {border-spacing: 0px;}\n" +
                "td { height:10px; width:10px; border: 1px solid; border-color: 202020; }\n" +
                ".alive {background-color: green;}\n" +
                ".dead {background-color: black;}\n" +
            "</style>\n" +
        "%1$s\n" +
        "</head>\n" +
        "<body>\n" +
            "%2$s\n%3$s\n%4$s" +
        "</body>\n" +
    "</html>"
    ;
    private static final String RELOAD =
    "<script type=\"text/javascript\">\n" +
            "window.onload=function(){setTimeout(function() { location.reload(true); }, 500);};" +
    "</script>\n"
    ;
    private static final String HEADER_TEMPLATE =
    "<h1>Generation: %1$05d</h1>"
    ;
    private static final String FOOTER_TEMPLATE =
    "<p>generated in %1$06dms</p>"
    ;
    private static final String BOARD_TEMPLATE =
    "<table>\n" +
        "<tbody>\n" +
            "%1$s\n" +
        "</tbody>\n" +
    "</table>"
    ;
    private static final String ROW_TEMPLATE =
    "<tr>%1$s</tr>\n"
    ;
    private static final String LIVE_CELL =
    "<td class=\"alive\"></td>"
    ;
    private static final String DEAD_CELL =
    "<td class=\"dead\"></td>"
    ;

    private final BoardService board;
    private final boolean reload;
    private final MonitoringService monitoring;

    private HttpServletResponse response;

    /**
     * Create new instance.
     * @param board provider of game data
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
        for (int y = 0; y < board.getSizeY(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < board.getSizeX(); x++) {
                row.append(board.getPoint(x,y) ? LIVE_CELL : DEAD_CELL);
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
