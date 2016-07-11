package rocks.bastion.core;

import rocks.bastion.core.event.*;
import rocks.bastion.core.model.JsonResponseDecoder;
import rocks.bastion.core.model.ResponseDecoder;
import rocks.bastion.core.model.StringResponseDecoder;

/**
 * The default implementation of a {@link BastionFactory} which registers a basic set of {@link ResponseDecoder model converters}. Also registers
 * an event listener to Bastion which will cause exceptions and errors thrown during a Bastion request to propagate up the
 * frame stack.
 * <br><br>
 * The set of model converters which are registered by this factory are listed below: <ul>
 *     <li>{@link StringResponseDecoder} - Binds the response to a {@link String} model by reading the content
 *     inside the HTTP response.
 *     <li>{@link JsonResponseDecoder} - Binds an HTTP response with content-type {@code application/json} to
 *     a Java-based object using <a href="https://github.com/google/gson">Gson</a>.</li>
 * </ul>
 */
public class DefaultBastionFactory extends BastionFactory implements BastionListener {

    @Override
    protected void prepareBastion(Bastion<?> bastion) {
        registerModelConverters(bastion);
        bastion.registerListener(this);
    }

    protected void registerModelConverters(Bastion<?> bastion) {
        bastion.registerModelConverter(new JsonResponseDecoder());
        bastion.registerModelConverter(new StringResponseDecoder());
    }

    @Override
    public void callStarted(BastionStartedEvent event) {

    }

    @Override
    public void callFinished(BastionFinishedEvent event) {

    }

    @Override
    public void callFailed(BastionFailureEvent event) {
        throw event.getAssertionError();
    }

    @Override
    public void callError(BastionErrorEvent event) {
        if (event.getThrowable() instanceof RuntimeException) {
            throw (RuntimeException) event.getThrowable();
        } else if (event.getThrowable() instanceof Error) {
            throw (Error) event.getThrowable();
        } else {
            throw new RuntimeException(event.getThrowable());
        }
    }
}