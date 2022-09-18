<?php
/*
 * Created by :
 * Pham Huu Thanh Dat | dat.pham@zalora.com
 */

if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

class User_model extends CI_Model
{

    /**
     * User_model constructor.
     */
    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('default', TRUE);
    }

    public function get_user($email)
    {
        $this->db->select("*");
        $this->db->from("icontract_user");
        $this->db->where("email", $email);

        $query = $this->db->get();
        return $query->result_array();
    }

    public function add_user($email)
    {
        $data = [
            "email" => $email,
        ];

        $this->db->insert("icontract_user", $data);
        return $this->db->insert_id();
    }

    public function add_user_info($user_info)
    {
        $this->db->insert("icontract_user_info", $user_info);
        return $this->db->insert_id();
    }

    public function get_user_info($fk_icontract_user)
    {
        $this->db->select("*");
        $this->db->from("icontract_user_info");
        $this->db->where("fk_icontract_user", $fk_icontract_user);

        $query = $this->db->get();
        return $query->result_array();
    }
}
