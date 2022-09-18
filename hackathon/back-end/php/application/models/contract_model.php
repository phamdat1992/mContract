<?php
/*
 * Created by :
 * Pham Huu Thanh Dat | dat.pham@zalora.com
 */

if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

class Contract_model extends CI_Model
{

    /**
     * Contract_model constructor.
     */
    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('default', TRUE);
    }

    public function get_all_contract($user_id)
    {
        $this->db->select("*");
        $this->db->from("icontract_document_sign");
        $this->db->join('icontract_document', 'icontract_document.id_icontract_document = icontract_document_sign.fk_icontract_document', 'left');
        $this->db->where("icontract_document_sign.fk_icontract_user", $user_id);

        $query = $this->db->get();
        return $query->result_array();
    }

    public function get_all_contract_need_to_sign($user_id)
    {
        $this->db->select("*");
        $this->db->from("icontract_document_sign");
        $this->db->join('icontract_document', 'icontract_document.id_icontract_document = icontract_document_sign.fk_icontract_document', 'left');
        $this->db->join('icontract_document_status', 'icontract_document_status.id_icontract_document_status = icontract_document_sign.fk_icontract_document_status');
        $this->db->where("icontract_document_sign.fk_icontract_user", $user_id);
        $this->db->where("icontract_document_status.id_icontract_document_status", 2);

        $query = $this->db->get();
        return $query->result_array();
    }

    public function get_all_contract_waiting_for_others($user_id)
    {
        $this->db->select("*");
        $this->db->from("icontract_document_sign");
        $this->db->join('icontract_document', 'icontract_document.id_icontract_document = icontract_document_sign.fk_icontract_document', 'left');
        $this->db->join('icontract_document_status', 'icontract_document_status.id_icontract_document_status = icontract_document_sign.fk_icontract_document_status');

        $query = $this->db->get();
        $data = $query->result_array();
        $filter = [];
        $result = [];

        foreach ($data as $contract) {
            if ($contract['fk_icontract_user'] == $user_id) {
                $filter[$contract['fk_icontract_document']] = $contract['fk_icontract_user'];
            }
        }

        foreach ($data as $contract) {
            if ($contract['fk_icontract_document_status'] == 2) {
                if (isset($filter[$contract['fk_icontract_document']])) {
                    if ($contract['fk_icontract_user'] != $user_id) {
                        $result[] = $contract;
                    }
                }
            }
        }

        return $result;
    }

    public function get_all_contract_complete($user_id)
    {
        $this->db->select("*");
        $this->db->from("icontract_document_sign");
        $this->db->join('icontract_document', 'icontract_document.id_icontract_document = icontract_document_sign.fk_icontract_document', 'left');
        $this->db->join('icontract_document_status', 'icontract_document_status.id_icontract_document_status = icontract_document_sign.fk_icontract_document_status');

        $query = $this->db->get();
        $data = $query->result_array();
        $filter = [];
        $result = [];

        foreach ($data as $contract) {
            if ($contract['fk_icontract_document_status'] == 1) {
                if (!isset($filter[$contract['fk_icontract_document']])) {
                    $filter[$contract['fk_icontract_document']] = true;
                }
            } else {
                $filter[$contract['fk_icontract_document']] = false;
            }
        }

        foreach ($data as $contract) {
            if (isset($filter[$contract['fk_icontract_document']]) && $filter[$contract['fk_icontract_document']] === true) {
                if ($contract['fk_icontract_user'] == $user_id)
                $result[] = $contract;
            }
        }

        return $result;
    }
}
