<?php
/**
 * User: Pham Huu Thanh Dat
 * Email: dat.pham@zalora.com
 * Date: 2019-04-09
 * Time: 13:59
 */

if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

include('./vendor/autoload.php');

use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;

class RabbitMQ
{

    private $connection;
    private $channel;

    public function __construct()
    {
        try {
            $this->connection = new AMQPStreamConnection(
                getenv('RABBITMQ_HOST', true),
                getenv('RABBITMQ_PORT', true),
                getenv('RABBITMQ_USER', true),
                getenv('RABBITMQ_PASS', true),
                getenv('RABBITMQ_VIRTUALHOST', true)
            );
            $this->channel = $this->connection->channel();
            $this->channel->set_ack_handler(
                function (AMQPMessage $message) {
                    // echo "Message acked with content " . $message->body . PHP_EOL;
                }
            );
            $this->channel->set_nack_handler(
                function (AMQPMessage $message) {
                    // echo "Message nacked with content " . $message->body . PHP_EOL;
                }
            );
        } catch (Exception $e) {
            echo $e->getMessage();
        }
    }

    public function pushMessage($message)
    {
        $this->channel->confirm_select();
        $message = new AMQPMessage(json_encode($message));
        $this->channel->basic_publish($message, "", getenv('RABBITMQ_ROUTINGKEY', true));
    }

    public function __destruct()
    {
        $this->channel->close();
        $this->connection->close();
    }
}
