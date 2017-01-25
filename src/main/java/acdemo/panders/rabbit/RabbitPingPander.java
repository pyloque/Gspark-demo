package acdemo.panders.rabbit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import gspark.core.background.Pander;
import gspark.core.rabbitmq.ExchangeType;
import gspark.core.rabbitmq.RabbitConsumer;
import gspark.core.rabbitmq.RabbitDelivery;
import gspark.core.rabbitmq.RabbitPublisher;
import gspark.core.rabbitmq.RabbitStore;

@Singleton
public class RabbitPingPander extends Pander<RabbitDelivery> {

	private final RabbitConsumer consumer;
	private final RabbitPublisher publisher;

	@Inject
	public RabbitPingPander(@Named("hello") RabbitStore rabbit) {
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
		System.out.printf("ping:%s\n", t.content());
		publisher.publish("r-ping-pong", "ping ping ping");
	}

	public void idle() {
		System.out.println("ping:idle");
	}

	@Override
	public void beforeStart() {
		consumer.exchange("ex-pong", ExchangeType.DIRECT, true);
		consumer.queue("q-pong", "r-pong-ping");
		publisher.exchange("ex-ping", ExchangeType.DIRECT, true);
		publisher.publish("r-ping-pong", "ping ping ping");
	}

}
