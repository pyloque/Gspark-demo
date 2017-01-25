package acdemo.panders.rabbit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ackern.core.background.Pander;
import ackern.core.rabbitmq.ExchangeType;
import ackern.core.rabbitmq.RabbitConsumer;
import ackern.core.rabbitmq.RabbitDelivery;
import ackern.core.rabbitmq.RabbitPublisher;
import ackern.core.rabbitmq.RabbitStore;

@Singleton
public class RabbitPongPander extends Pander<RabbitDelivery> {

	private final RabbitConsumer consumer;
	private final RabbitPublisher publisher;

	@Inject
	public RabbitPongPander(@Named("hello") RabbitStore rabbit) {
		super(5);
		consumer = new RabbitConsumer(rabbit);
		publisher = new RabbitPublisher(rabbit);
	}

	@Override
	public RabbitDelivery take() {
		return consumer.poll(true);
	}

	@Override
	public void process(RabbitDelivery t) {
		System.out.printf("pong:%s\n", t.content());
		publisher.publish("r-pong-ping", "pong pong pong");
	}

	public void idle() {
		System.out.println("pong:idle");
	}

	@Override
	public void beforeStart() {
		consumer.exchange("ex-ping", ExchangeType.DIRECT, true);
		consumer.queue("q-ping", "r-ping-pong");
		publisher.exchange("ex-pong", ExchangeType.DIRECT, true);
	}

}
