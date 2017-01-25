package acdemo.workers.rabbit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import gspark.core.background.Worker;
import gspark.core.rabbitmq.ExchangeType;
import gspark.core.rabbitmq.RabbitConsumer;
import gspark.core.rabbitmq.RabbitDelivery;
import gspark.core.rabbitmq.RabbitPublisher;
import gspark.core.rabbitmq.RabbitStore;

@Singleton
public class RabbitPongWorker extends Worker<RabbitDelivery> {

	private final RabbitConsumer consumer;
	private final RabbitPublisher publisher;

	@Inject
	public RabbitPongWorker(@Named("hello") RabbitStore rabbit) {
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
