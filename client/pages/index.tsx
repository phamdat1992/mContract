import Head from "next/head";
import Layout from "@/components/Layout";
import { ComponentProps, useEffect, useState } from "react";
import { GetStaticProps } from "next";
import Link from 'next/link';
import ContactForm from "@/components/ContactForm";
import * as AOS from 'aos';
import 'aos/dist/aos.css';
import Footer from "@/components/Footer";
import SuccessToast from '@/components/SuccessToast';

const Home = (props: ComponentProps<any>) => {
   const [name, setName] = useState('');
   const [showToastSucces, setShowToastSuccess] = useState(false);
   const changeForm = (status: boolean) => {
      setShowToastSuccess(status);
   }
   useEffect(() => {
      AOS.init();
   }, []);
   return (
      <Layout>
         <Head>
            <title>MContract</title>
         </Head>
         <div className="info py-5" id="about">
            <div className="container">
               <div className="row info" data-aos="fade-up">
                  <div className="col-12 col-lg-7">
                     <div className="text_info intro-info">
                        <div className="title mb-2">
                           <p>Về chúng tôi</p>
                           <h2>Gắn kết doanh nghiệp, ký số mọi lúc, mọi nơi</h2>
                        </div>
                        <p>
                           MContract là giải pháp ký số hàng đầu cho doanh nghiệp
                           <br />
                           - Ký số không giấy tờ
                           <br />
                           - Ký mọi nơi không gián đoạn
                           <br />
                           - Không gặp mặt, không phát sinh chi phí
                        </p>
                        <button className="btn btn_outline_main mr-3">Về chúng tôi</button>&nbsp;
                        <a className="btn btn_new" title="Dùng thử" href="https://mcontract.vn/dang-ki">Dùng thử</a>
                     </div>
                  </div>
                  <div className="col-12 col-lg">
                     <div className="img_info mt-3 mt-lg-0 text-center">
                        <img className="img-fluid" src="/images/intro/ve_chung_toi.svg" alt="" loading="lazy" />
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <div className="user_guide py-5">
            <div className="container">
               <div className="row" data-aos="fade-up">
                  <div className="col-md-5 col-lg-4 col-xl-3">
                     <div className="ug_img">
                        <img className="img-fluid rounded-circle" src="/images/intro/userguide.webp" alt="" loading="lazy" />
                     </div>
                  </div>
                  <div className="col-sm">
                     <div className="title mb-2">
                        <p>Hướng dẫn sử dụng</p>
                        <h2>Sử dụng MContract đơn giản - hiệu quả</h2>
                     </div>
                     <div className="adv_item row">
                        <div className="col-auto adv_icon pr-0">
                           <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor" className="bi bi-check2-circle" viewBox="0 0 16 16">
                              <path d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                              <path d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                           </svg>
                        </div>
                        <div className="col d-flex align-items-center intro-info">
                           Hệ thống đơn giản, ít thao tác, thuận tiện cho mọi đối tượng. Các luồng tạo hợp đồng trực quan, logic.
                        </div>
                     </div>

                     <div className="adv_item row">
                        <div className="col-auto adv_icon pr-0">
                           <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor" className="bi bi-check2-circle" viewBox="0 0 16 16">
                              <path d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                              <path d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                           </svg>
                        </div>
                        <div className="col d-flex align-items-center intro-info">
                           Dễ dàng kết nối với mọi doanh nghiệp ở khắp nơi. Doanh nghiệp chuyển mình thành doanh nghiệp số hiện đại, không giấy tờ, không phát sinh chi phí đi lại giao dịch.
                        </div>
                     </div>
                     <Link href="hoi-dap">
                        <button className="btn btn_new mt-2">Tìm hiểu thêm</button>
                     </Link>
                  </div>
               </div>
            </div>
         </div>
         <div className="ms_step py-5" id="signature-process">
            <div className="container">
               <div className="row" data-aos="fade-up">
                  <div className="col-12 col-lg-4">
                     <div className="title mb-2">
                        <h2>3 bước ký hợp đồng điện tử</h2>
                     </div>
                     <p className="intro-info">Quy trình 3 bước ký hợp đồng điện tử đơn giản bằng hệ thống ký số MContract</p>
                     <Link href="hoi-dap">
                        <button className="btn btn_new mb-4 mb-lg-0">Tìm hiểu thêm</button>
                     </Link>
                  </div>
                  <div className="col-12 col-lg">
                     <div className="row">
                        <div className="col-12 col-md mb-4 mb-md-0">
                           <div className="ms_item">
                              <div className="ms_number">1</div>
                              <h5>Tải hợp đồng</h5>
                              <p className="intro-info">- Kiểm tra hợp đồng</p>
                              <p className="intro-info">- Tải hợp đồng lên hệ thống</p>
                           </div>
                        </div>
                        <div className="col-12 col-md mb-4 mb-md-0">
                           <div className="ms_item">
                              <div className="ms_number">2</div>
                              <h5>Lựa chọn đối tác</h5>
                              <p className="intro-info">- Lựa chọn đối tác ký hợp đồng</p>
                              <p className="intro-info">- Xác định vị trí chữ ký cho từng đối tác</p>
                           </div>
                        </div>
                        <div className="col-12 col-md">
                           <div className="ms_item">
                              <div className="ms_number">3</div>
                              <h5>Gửi và hoàn thiện</h5>
                              <p className="intro-info">- Nhập địa chỉ email của đối tác</p>
                              <p className="intro-info">- Nhập tiêu đề, nội dung và gửi hợp đồng</p>
                              <p className="intro-info">- Chờ ký và hoàn thành</p>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <div className="install py-4" id="tool">
            <div className="container">
               <div className="row" data-aos="fade-up">
                  <div className="col-12 col-md">
                     <div className="title mb-2">
                        <p>Công cụ ký số</p>
                        <h2>Công cụ đa nền tảng, dễ dàng cài đặt</h2>
                     </div>
                     <p className="mb-3 mb-md-0 intro-info">Hỗ trợ mọi loại USB Token</p>
                  </div>
                  <div className="col-12 col-md-auto d-flex align-items-center">
                     <Link href="hoi-dap">
                        <button className="btn btn-light mr-3">Hướng dẫn cài đặt</button>
                     </Link>
                     <a type="buton" href="https://mcontract.vn/uploads/MContract.zip" download className="btn btn_orange">Tải công cụ ký số</a>
                  </div>
               </div>
            </div>
         </div>
         <div className="benefit py-5">
            <div className="container">
               <div className="title mb-2 text-center">
                  <p>Đảm bảo hệ thống</p>
                  <h2>Nhanh, đúng pháp lý và an toàn</h2>
               </div>
               <div className="row" data-aos="fade-up">
                  <div className="col-12 col-md mb-4 mb-md-0 intro-info">
                     <div className="be_item h-100">
                        <div className="be_img">
                           <img src="/images/intro/be1.webp" alt="" loading="lazy" />
                        </div>
                        <h5>Tối ưu thời gian, chi phí</h5>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Tối ưu thời gian ký kết hợp đồng</span>
                           </div>
                        </div>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Không chi phí in ấn, chuyển phát</span>
                           </div>
                        </div>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Không phát sinh chi phí lưu trữ hồ sơ</span>
                           </div>
                        </div>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Tạo dấu ấn chuyên nghiệp với khách hàng</span>
                           </div>
                        </div>
                        <div className="be_icon shadow">
                           <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" fill="currentColor"
                              className="bi bi-hourglass-split" viewBox="0 0 16 16">
                              <path
                                 d="M2.5 15a.5.5 0 1 1 0-1h1v-1a4.5 4.5 0 0 1 2.557-4.06c.29-.139.443-.377.443-.59v-.7c0-.213-.154-.451-.443-.59A4.5 4.5 0 0 1 3.5 3V2h-1a.5.5 0 0 1 0-1h11a.5.5 0 0 1 0 1h-1v1a4.5 4.5 0 0 1-2.557 4.06c-.29.139-.443.377-.443.59v.7c0 .213.154.451.443.59A4.5 4.5 0 0 1 12.5 13v1h1a.5.5 0 0 1 0 1h-11zm2-13v1c0 .537.12 1.045.337 1.5h6.326c.216-.455.337-.963.337-1.5V2h-7zm3 6.35c0 .701-.478 1.236-1.011 1.492A3.5 3.5 0 0 0 4.5 13s.866-1.299 3-1.48V8.35zm1 0v3.17c2.134.181 3 1.48 3 1.48a3.5 3.5 0 0 0-1.989-3.158C8.978 9.586 8.5 9.052 8.5 8.351z" />
                           </svg>
                        </div>
                     </div>
                  </div>
                  <div className="col-12 col-md mb-4 mb-md-0 intro-info">
                     <div className="be_item h-100">
                        <div className="be_img">
                           <img src="/images/intro/be2.webp" alt="" loading="lazy" />
                        </div>
                        <h5>Đáp ứng tiêu chuẩn pháp lý về ký số</h5>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Đáp ứng luật giao dịch điện tử số 51/2005/QH11 ngày 29/11/2005</span>
                           </div>
                        </div>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Đáp ứng với mọi loại USB Token</span>
                           </div>
                        </div>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Năm 2019 Bộ luật lao động ghi nhận hình thức ký hợp đồng điện tử</span>
                           </div>
                        </div>
                        <div className="be_icon shadow">
                           <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" fill="currentColor"
                              className="bi bi-bookmark-check" viewBox="0 0 16 16">
                              <path fillRule="evenodd"
                                 d="M10.854 5.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 7.793l2.646-2.647a.5.5 0 0 1 .708 0z" />
                              <path
                                 d="M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5V2zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1H4z" />
                           </svg>
                        </div>
                     </div>
                  </div>
                  <div className="col-12 col-md mb-4 mb-md-0 intro-info">
                     <div className="be_item h-100">
                        <div className="be_img">
                           <img src="/images/intro/be3.webp" alt="" loading="lazy" />
                        </div>
                        <h5>Bảo mật tuyệt đối</h5>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Đảm bảo an ninh tối đa</span>
                           </div>
                        </div>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Đảm bảo không làm lộ thông tin doanh nghiệp</span>
                           </div>
                        </div>
                        <div className="be_text row">
                           <div className="col-auto pr-0">
                              <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                                 className="bi bi-check2-circle" viewBox="0 0 16 16">
                                 <path
                                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                 <path
                                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                              </svg>
                           </div>
                           <div className="col pl-2">
                              <span>Đầy đủ tính pháp lý</span>
                           </div>
                        </div>
                        <div className="be_icon shadow">
                           <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" fill="currentColor"
                              className="bi bi-shield-check" viewBox="0 0 16 16">
                              <path
                                 d="M5.338 1.59a61.44 61.44 0 0 0-2.837.856.481.481 0 0 0-.328.39c-.554 4.157.726 7.19 2.253 9.188a10.725 10.725 0 0 0 2.287 2.233c.346.244.652.42.893.533.12.057.218.095.293.118a.55.55 0 0 0 .101.025.615.615 0 0 0 .1-.025c.076-.023.174-.061.294-.118.24-.113.547-.29.893-.533a10.726 10.726 0 0 0 2.287-2.233c1.527-1.997 2.807-5.031 2.253-9.188a.48.48 0 0 0-.328-.39c-.651-.213-1.75-.56-2.837-.855C9.552 1.29 8.531 1.067 8 1.067c-.53 0-1.552.223-2.662.524zM5.072.56C6.157.265 7.31 0 8 0s1.843.265 2.928.56c1.11.3 2.229.655 2.887.87a1.54 1.54 0 0 1 1.044 1.262c.596 4.477-.787 7.795-2.465 9.99a11.775 11.775 0 0 1-2.517 2.453 7.159 7.159 0 0 1-1.048.625c-.28.132-.581.24-.829.24s-.548-.108-.829-.24a7.158 7.158 0 0 1-1.048-.625 11.777 11.777 0 0 1-2.517-2.453C1.928 10.487.545 7.169 1.141 2.692A1.54 1.54 0 0 1 2.185 1.43 62.456 62.456 0 0 1 5.072.56z" />
                              <path
                                 d="M10.854 5.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 7.793l2.646-2.647a.5.5 0 0 1 .708 0z" />
                           </svg>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <div className="standard py-5">
            <div className="container">
               <div className="row" data-aos="fade-up">
                  <div className="col-12 col-sm intro-info">
                     <div className="title mb-2">
                        <p>Tiêu chuẩn ký số</p>
                        <h2>Tiêu chuẩn ký số châu Âu</h2>
                     </div>
                     <p>MContract là hệ thống ký hợp đồng điện tử theo tiêu chuẩn châu Âu. Hệ thống cam kết mang đầy
                        đủ tính pháp lý của luật pháp Việt Nam</p>
                     <Link href="https://ec.europa.eu/cefdigital/wiki/display/CEFDIGITAL/eSignature">
                        <a className="btn btn_new" target="_blank">Tìm hiểu thêm</a>
                     </Link>
                  </div>
                  <div className="col-12 col-sm-auto">
                     <div className="std_img">
                        <img className="img-fluid" src="/images/intro/standard1.svg" alt="" loading="lazy" />
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <div className="contact py-5" id="contact">
            <div className="container">
               <div className="row" data-aos="fade-up">
                  <div className="col-12 col-md intro-info">
                     <div className="title mb-2">
                        <p>Liên hệ</p>
                        <h2>Hãy gửi những thắc mắc cho chúng tôi</h2>
                     </div>
                     <ContactForm changeForm={changeForm}></ContactForm>
                  </div>
                  <div className="col-12 col-md">
                     <div className="cnt_img text-center">
                        <img className="img-fluid" src="/images/intro/contact.svg" alt="" loading="lazy" />
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <div className="jab py-4">
            <div className="container">
               <div className="row" data-aos="fade-up">
                  <div className="col-12 col-lg-6 d-flex align-items-center">
                     <img className="img-fluid mb-3 mb-lg-0" src="/images/intro/jab.svg" alt="" loading="lazy" />
                  </div>
                  <div className="col-12 col-lg-6 d-flex align-items-center">
                     <div className="w-100">
                        <div className="title mb-2">
                           <h2>Ký hợp đồng 3 không 3 có</h2>
                        </div>
                        <div className="row mt-3 intro-info jab-color">
                           <div className="col-12 col-sm-6">
                              <div className="row jab_text">
                                 <div className="col-auto pr-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26"
                                       fill="currentColor" className="bi bi-x-circle" viewBox="0 0 16 16">
                                       <path
                                          d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
                                       <path
                                          d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z" />
                                    </svg>
                                 </div>
                                 <div className="col pl-2">
                                    <span>Không gián đoạn</span>
                                 </div>
                              </div>
                              <div className="row jab_text">
                                 <div className="col-auto pr-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26"
                                       fill="currentColor" className="bi bi-x-circle" viewBox="0 0 16 16">
                                       <path
                                          d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
                                       <path
                                          d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z" />
                                    </svg>
                                 </div>
                                 <div className="col pl-2">
                                    <span>Không giấy tờ, chuyển phát</span>
                                 </div>
                              </div>
                              <div className="row jab_text">
                                 <div className="col-auto pr-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26"
                                       fill="currentColor" className="bi bi-x-circle" viewBox="0 0 16 16">
                                       <path
                                          d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
                                       <path
                                          d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z" />
                                    </svg>
                                 </div>
                                 <div className="col pl-2">
                                    <span>Không tiếp xúc</span>
                                 </div>
                              </div>
                           </div>
                           <div className="col-12 col-sm-6 pl-md-0">
                              <div className="row jab_text">
                                 <div className="col-auto pr-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26"
                                       fill="currentColor" className="bi bi-check2-circle" viewBox="0 0 16 16">
                                       <path
                                          d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                       <path
                                          d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                                    </svg>
                                 </div>
                                 <div className="col pl-2">
                                    <span>Có đầy đủ pháp lý</span>
                                 </div>
                              </div>
                              <div className="row jab_text">
                                 <div className="col-auto pr-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26"
                                       fill="currentColor" className="bi bi-check2-circle" viewBox="0 0 16 16">
                                       <path
                                          d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                       <path
                                          d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                                    </svg>
                                 </div>
                                 <div className="col pl-2">
                                    <span>Có bảo mật tuyệt đối</span>
                                 </div>
                              </div>
                              <div className="row jab_text">
                                 <div className="col-auto pr-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26"
                                       fill="currentColor" className="bi bi-check2-circle" viewBox="0 0 16 16">
                                       <path
                                          d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0z" />
                                       <path
                                          d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l7-7z" />
                                    </svg>
                                 </div>
                                 <div className="col pl-2">
                                    <span>Có tạo dấu ấn chuyên nghiệp</span>
                                 </div>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <Footer page="home" />
         {showToastSucces && <SuccessToast />}

      </Layout>
   );
};

const getStaticProps: GetStaticProps = async (context) => {
   return {
      props: {
         staticProps: "Static Props",
      },
   };
};

// const getServerSideProps: GetServerSideProps = async (context: GetServerSidePropsContext) => {
//   console.log(context);
//   return {
//     props: {
//       serverSideProps: 'Server Side Props',
//     }
//   }
// }

export { getStaticProps };
export default Home;
