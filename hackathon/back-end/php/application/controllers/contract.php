<?php

/*
 * Created by :
 * Pham Huu Thanh Dat | dat.pham@zalora.com
 */

if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

session_start();

class Contract extends CI_Controller
{
    public function __construct()
    {
        parent:: __construct();
        $this->load->model(
            [
                'contract_model',
                'user_model'
            ]
        );
    }

    public function get_all_contract()
    {
        $user = $this->user_model->get_user($_GET['email']);

        if (count($user) == 1) {
            $user = reset($user);
            echo json_encode(
                $this->contract_model->get_all_contract(
                    $user['id_icontract_user']
                )
            );
        }
    }

    public function get_all_contract_need_to_sign()
    {
        $user = $this->user_model->get_user($_GET['email']);

        if (count($user) == 1) {
            $user = reset($user);
            echo json_encode(
                $this->contract_model->get_all_contract_need_to_sign(
                    $user['id_icontract_user']
                )
            );
        }
    }

    public function get_all_contract_waiting_for_others()
    {
        $user = $this->user_model->get_user($_GET['email']);

        if (count($user) == 1) {
            $user = reset($user);
            echo json_encode(
                $this->contract_model->get_all_contract_waiting_for_others(
                    $user['id_icontract_user']
                )
            );
        }
    }

    public function get_all_contract_complete()
    {
        $user = $this->user_model->get_user($_GET['email']);

        if (count($user) == 1) {
            $user = reset($user);
            echo json_encode(
                $this->contract_model->get_all_contract_complete(
                    $user['id_icontract_user']
                )
            );
        }
    }
}
