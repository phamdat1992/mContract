<?php if (!defined('BASEPATH')) exit('No direct script access allowed');

/*
 * Created by :
 * Pham Huu Thanh Dat | dat.pham@zalora.com
 * Copyright © 2019 Zalora vn
 */

/*
|--------------------------------------------------------------------------
| Copycat
|--------------------------------------------------------------------------*/
$config['copycat_port'] = '3306';
$config['copycat_host'] = getenv('COPYCAT_HOST', true);
$config['copycat_user'] = getenv('COPYCAT_USERNAME', true);
$config['copycat_password'] = getenv('COPYCAT_PASSWORD', true);
$config['copycat_ssl_key'] = '';
$config['copycat_ssl_cert'] = '';
$config['copycat_ssl_ca'] = '';

/*
|--------------------------------------------------------------------------
| Google OAuth2
|--------------------------------------------------------------------------*/
$config['client_id'] = getenv('GOOGLE_OAUTH_CLIENT_ID', true);
$config['client_secret'] = getenv('GOOGLE_OAUTH_CLIENT_SECRET', true);
$config['redirect_uri'] = getenv('GOOGLE_OAUTH_REDIRECT_URL_AGENT', true);

$config['simple_api_key'] = getenv('GOOGLE_API_KEY', true);
$config['redirect_uri_pickup'] = getenv('GOOGLE_OAUTH_REDIRECT_URL_PICKUP', true);

/* End of file params.php */
/* Location: ./application/config/params.php */