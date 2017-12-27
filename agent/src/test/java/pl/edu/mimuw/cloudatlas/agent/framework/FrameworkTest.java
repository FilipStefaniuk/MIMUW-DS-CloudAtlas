package pl.edu.mimuw.cloudatlas.agent.framework;

import org.junit.Test;

public class FrameworkTest {

    @Module(name = "Greeter")
    public static class Greeter extends ModuleBase {

        private static final int HANDLE_ID = 1;
        private static final Integer MAX = 5;

        private Integer counter = 0;

        @Handler(id = HANDLE_ID)
        MessageHandler<?> handler1 = new MessageHandler<Message>() {

            @Override
            protected void handle(Message message) {
                System.out.println(Thread.currentThread().getId() + ": Hello!");
                counter++;
                try {
                    if (counter > MAX)
                        shutdown();
                    else{
                        sendMessage(new Address(Greeter.class, Greeter.HANDLE_ID), new SimpleMessage());
                    }
                } catch (InterruptedException e) {}
            }
        };
    }

    private static class SimpleMessage extends Message {}

//  --------------------------------------------------------------------------------------------------------------------

    @Test
    public void simpleTest() throws Exception{
        EventQueue eventQueue = EventQueue.builder()
                .executor(Greeter.class)
                .executor(Greeter.class)
                .executor(Greeter.class)
                .build();

        SimpleMessage message = new SimpleMessage();
        message.setAddress(new Address(Greeter.class, Greeter.HANDLE_ID));
        eventQueue.sendMessage(message);
        eventQueue.start();
    }
}
