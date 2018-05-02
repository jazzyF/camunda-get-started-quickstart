/*
 * Copyright © 2018 camunda services GmbH and various authors (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.getstarted.chargecard;

import java.util.logging.Logger;

import org.camunda.bpm.client.ExternalTaskClient;

public class ChargeCardWorker {
	private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());

	public static void main(String[] args) {
		ExternalTaskClient client = ExternalTaskClient.create()
				.baseUrl("http://localhost:8080/engine-rest")
				.asyncResponseTimeout(10000) // long polling timeout
				.build();

		// subscribe to an external task topic as specified in the process
		client.subscribe("charge-card")
				.lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
				.handler((externalTask, externalTaskService) -> {
					// Put your business logic here

					// Get a process variable
					String item = (String) externalTask.getVariable("item");
					Long amount = (Long) externalTask.getVariable("amount");
					LOGGER.info("Charging credit card with an amount of '" + amount + "'€ for the item '" + item + "'...");

					// Complete the task
					externalTaskService.complete(externalTask);
				})
				.open();
	}
}