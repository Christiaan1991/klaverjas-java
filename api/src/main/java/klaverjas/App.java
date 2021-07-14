package klaverjas;

import klaverjas.ws.EventSocket;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.glassfish.jersey.servlet.ServletContainer;
import java.time.Duration;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = startServer(8080);
        ServletContextHandler context = createStatefulContext(server);
        registerServlets(context);

        server.start();
        System.out.println("Started server.");
        System.out.println("Listening on http://localhost:8080/");
        System.out.println("Press CTRL+C to exit.");
        server.join();
    }

    private static Server startServer(int port) {
        return new Server(8080);
    }

    private static ServletContextHandler createStatefulContext(Server server) {
        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        return context;
    }

    private static void registerServlets(ServletContextHandler context) {
        // Use the Jersey framework to translate the classes in the
        // klaverjas.api package to server endpoints (servlets).
        // For example, the StartKlaverjas class will become an endpoint at
        // http://localost:8080/klaverjas/api/start
        ServletHolder serverHolder = context.addServlet(ServletContainer.class, "/klaverjas/api/*");
        serverHolder.setInitOrder(1);
        serverHolder.setInitParameter("jersey.config.server.provider.packages",
                "klaverjas.api");

        // Configure specific websocket behavior
        JettyWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) ->
        {
            // Configure default max size
            wsContainer.setMaxTextMessageSize(65535);

            //set IdleTimeout to 5 minutes
            wsContainer.setIdleTimeout(Duration.ofHours(6));

            // Add websockets
            wsContainer.addMapping("/events/*", EventSocket.class);
        });

    }
}
