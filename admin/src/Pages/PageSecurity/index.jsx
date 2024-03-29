import HeaderPolicy from '@Components/HeaderPolicy';
import React, { useEffect } from 'react';
import SimpleBar from "simplebar-react";

PageSecurity.defaultProps = {
    title: "Chính sách bảo mật",
    titleEn: "Privacy Policy"
};

function PageSecurity(props) {
    useEffect(() => {
        document.title = "MContract"
    }, []);
    return (
        <div className="search-policy">
            <HeaderPolicy title={props.title} titleEn={props.titleEn} />
            <SimpleBar className="container-fluid px-24px" id="page_content">
                <div className="content_en">

                    <p>At MContract, protecting the User's personal Data is the main priority. We understand that the User's Data legally belongs to the User and is protected by law. Therefore, MContract only collects, stores, and processes the User’s Data under the consent and permission of the User.
                        In addition to agreeing to the Terms of Use, the User must carefully read the following privacy policies by MContract to access and use the Service. The registration for the Service offered by MContract means the User has agreed and accepted the terms of this Privacy Policy.
                        The Privacy Policy explains how MContract collects, stores, and processes Data when the User accesses or uses our Services.
                    </p>
                    <h3> DEFINITION/GLOSSARY</h3>
                    <p><b><i>“MContract”</i></b>, also know as "ONLINE TECHNOLOGY SERVICES COMPANY LIMITED". In this Privacy Policy, MContract may be referred to as "We" depending on the context.
                    </p>
                    <p>
                        <b><i>“Service”</i></b> means any product, Service, content, feature, technology, or functionality, as well as all related webpages and relevant applications made available to Users by MContract.
                    </p>
                    <p><b><i>“User”</i></b> means the person/organization that uses the Service.

                    </p>
                    <p>
                        <b><i>“Data”</i></b> may include first name and last name, address, phone number, email address, gender, date of birth, identity card number or identification number, date of issue, User photo ID, digital signature information, corporate/organization logo (for organizations). Data does not include information that does not identify a particular User.
                    </p>

                    <h3>  PURPOSE AND SCOPE OF INFORMATION COLLECTION</h3>
                    <p> Data collection aims to:
                    </p><p> - Comply with the legal regulations, perform legal obligations required to provide the Data system.
                    </p><p> - Implement processes and procedures necessary to conduct our business purposes and serve the interests of the Users.
                    </p><p> - Guarantee the storage of the User's information system in MContract's system.
                    </p><p>  - Secure a groundwork for MContract to solve problems and disputes (if any) that occur in the future.
                    </p><p>To the extent of the rights to collect User's information, MContract only collects Data when a User accesses or uses our Services, including the following information:
                    </p><p> - Registration information: When a User registers to use our Service by creating an account, we will collect the necessary Data to perform the requested Service, including full name, address, phone number, email address, gender, date of birth, identity card number or identification number, date of issue, User photo ID, digital signature information, business/organization logo (for organizations). With this information, Users are solely responsible for the security and storage of all activities related to the Service. MContract may ask you to provide additional Data in the course of using the Service.
                    </p><p> - Transaction information and experience: When Users use the payment methods for the MContract Service, we will collect information about these transactions, as well as other information related to the transaction, including but not limited to: the money amount paid for the Services, billing account information, payment method used to complete the transaction, device information, technical usage information, and location information.
                    </p><p> - Information you choose to provide us in order to obtain certain additional Services: when a User requests additional Services or other optional functions, we may need to collect additional information.
                    </p><p> - Other information regarding the User's usage of the Services by MContract: when the User contacts MContract, MContract customer Service department, or responds to a survey, we may collect additional necessary information from the User, or about the User.

                    </p>
                    <h3>COLLECTION METHODM</h3>
                    <p>When you visit and use Services by MContract, we may use Cookies and other tracking technologies (collectively referred to as "Cookies") to recognize and customize the User's online experience: the Services in use, online content, and advertising; to measure the effectiveness of promotions and perform analysis; and to minimize risks, prevent potential fraud, and improve the reliability and safety on Services by MContract.
                    </p><p>  To avoid confusion, the use of Cookies is only performed with the User's consent. However, be noted that some features of our Services are only available only with the use of Cookies. Therefore, if you choose to disable or decline Cookie, the Services may be limited or impossible to process for you.

                    </p>
                    <h3>SCOPE OF DATA USAGE</h3>
                    <p>MContract only uses the Data with the consent of the User, to carry the purposes described in this Privacy Policy, or when MContract determines it is necessary for the purposes of the legitimate interests of the parties involved. Including:
                    </p><p> - Service operation and provision: the collection of Data enables MContract to regularly communicate with Users regarding the latest Services, updates, upcoming events of MContract. To avoid confusion, the User has the right to choose to participate in the MContract mailing list or not.
                    </p><p> - Delivery of important notices regarding Service: Data collection enables MContract to send important notices such as price information, changes in terms & conditions, and changes in policies. Since this is important information regarding the activities of the involved parties, the User cannot refuse to receive these notices.
                    </p><p> - Personalization of User experience: Data collection enables  MContract to propose and provide Users with the appropriate Services, selection and promotion.
                    </p><p> - Respond to a User's immediate request, for example, to contact the User about a question sent to our customer Service.
                    </p><p>- Risk management and protection of Services and Users from fraud by verifying User identity. Data collection along with device information, technical information, and location information enable MContract to detect and prevent fraud and Service abuse.
                    </p><p> - Manage and develop business operations: Data collection enables MContract to track, analyze and improve the Service, customer care process.
                    </p><p>- Fulfill our obligations to this Privacy Policy as well as to all applicable laws and regulations.

                    </p>
                    <h3>DATA SHARING</h3>
                    <p>MContract commits to not provide, share or distribute the Data under our management to any third party, except with the consent of the User.
                    </p><p>In some cases, MContract may disclose Data to a third party, with the following specifics:
                    </p><p> - Under the User’s consent.
                    </p><p> - Meet the requirements of the User, including but not limited to the payment linkage, delivery of contracts, documents related to the Service at the request of the User. In these cases, we require the third party to ensure the processing of this Data in accordance with relevant laws.
                    </p><p> - In accordance with the law, legal process, litigation and/or required by the competent authority.
                    </p><p>- Where MContract believes or has reason to believe that the disclosure of the Data is necessary or appropriate to detect, prevent and respond to fraud, unauthorized use of the Service, or violation toward our terms or policies or other illegal or harmful operation; to protect ourselves (including rights, property, Services, prevention of financial loss or physical damage), you or others; to comply with national security, law enforcement or other matters.
                    </p><p> - In addition, in the event of restructure or merger, MContract may transfer any of all Data we collected to the relevant third party.

                    </p>
                    <h3>DATA RETENTION TIME</h3>
                    <p>The storage of Data will be confidential, Data storage on MContract's server is permanent.
                    </p>
                    <h3>MEASURES AND TOOLS FOR USERS TO ACCESS AND EDIT DATA</h3>
                    <p>The User has the right to check, update, and adjust their personal information by logging into their account and editing personal information or requesting MContract to do so.
                    </p>

                    <h3>DATA SECURITY COMMITMENT</h3>
                    <p>MContract always gives the confidentiality of User Data the main priority. We maintain technical, physical, and administrative security measures designed to provide reasonable protection of User Data from loss, misuse, unauthorized access, reveal and change.
                    </p><p> - MContract commits to absolutely secure User’s Data confidentiality under this Privacy Policy. The collection and usage of User’s Data are performed only with the consent of the User or upon the request of a competent authority.
                    </p><p> - MContract does not use, transfer, provide or disclose to any third party about the Data without permission from the User.
                    </p><p> - In the event that the Data storage server is attacked by the hackers, resulting in Data loss, MContract will be responsible for reporting the incident to the competent authority for prompt investigation and notification to the User. MContract will not hold any responsibility regarding this case.
                    </p><p>- MContract requires individuals when registering as a member, to provide all relevant personal information such as full name, contact address, email, identity card number, phone ..., and take the responsibility for the legality of the above information. MContract does not take any responsibility or resolve any claims/dispute related to the interests of that User given all information provided by the User in the initial registration is inaccurate.
                    </p><p>  - Users have the right to submit complaints about the disclosure of personal information to third parties to MContract. When receiving these claims, MContract will validate the information, be responsible for responding to the User’s claim regarding the reason and guide Users to recover and secure the information.
                    </p><p>  - In case MContract has modifications or supplements, it is not obligated to notify the User in advance, the modification happens in a default manner. Users must regularly check for updates to this Privacy Policy. In case MContract updates the Privacy Policy and the User continues to use the Service after the update, it means that the User agrees to the new term(s) provided in the update.
                    </p><p> - While we are committed to protecting the systems and Services, Users are still responsible for protecting and maintaining the privacy of their own passwords and account/profile registration information and verification, proven that the Data we hold is accurate and is always up to date.
                    </p><p> - Users are solely responsible for personal information when sharing products and Services of MContract on other platforms. Please be careful when sharing this information.
                    </p>

                    <h3>TERMS AND CONDITIONS</h3>
                    <p> Users are required to read and agree to these Terms and Conditions before using the Service.
                    </p><p>The Terms and Conditions govern the rights and obligations of the User, as a customer, to use the Services provided by MContract.
                    </p>
                </div>
                <div className="content_vi">
                    <p>Tại MContract việc bảo vệ dữ liệu cá nhân của Người Dùng là ưu tiên hàng đầu. Chúng tôi hiểu rằng, các dữ liệu của Người Dùng là thuộc quyền sở hữu hợp pháp và được pháp luật bảo vệ. Chính vì vậy, việc thu thập, lưu trữ và xử lý dữ liệu cá nhân chỉ được MContract thực hiện khi có sự đồng ý và cho phép của Người Dùng.
                    </p><p>  Để truy cập và sử dụng Dịch Vụ ngoài việc đồng ý về điều khoản sử dụng thì Người Dùng còn phải đọc kỹ những chính sách bảo mật của MContract sau đây. Việc đăng ký sử dụng Dịch Vụ của MContract cũng có nghĩa rằng Người Dùng đã đồng ý và chấp thuận ràng buộc bởi các điều khoản của bản Chính Sách Bảo Mật này.
                    </p><p> Chính Sách Bảo Mật giải thích cách MContract thu thập, lưu giữ và xử lý Dữ Liệu trong quá trình Người Dùng truy cập hoặc sử dụng Dịch Vụ của Chúng Tôi.
                    </p>
                    <h3>ĐỊNH NGHĨA</h3>
                    <p>Các thuật ngữ viết hoa trong Chính Sách Bảo Mật này có ý nghĩa như sau:
                    </p><p> <b><i>“MContract”</i></b> nghĩa là Công Ty TNHH Dịch Vụ Công Nghệ Trực Tuyến. Trong Chính Sách Bảo Mật này,  MContract có thể  được đề cập là “Chúng Tôi” tùy theo ngữ cảnh.
                    </p><p> <b><i>“Dịch Vụ”</i></b> nghĩa là mọi sản phẩm, Dịch Vụ, nội dung, tính năng, công nghệ hay chức năng, cũng như tất cả các trang web, ứng dụng liên quan do MContract cung cấp cho Người Dùng.
                    </p><p><b><i>“Người Dùng”</i></b> nghĩa là cá nhân/tổ chức sử dụng Dịch Vụ.

                    </p><p><b><i>“Dữ Liệu”</i></b> có thể bao gồm họ và tên, địa chỉ, số điện thoại, địa chỉ email, giới tính, ngày sinh, số chứng minh nhân dân hoặc căn cước công dân, ngày cấp, hình nhận diện Người Dùng, thông tin chữ ký số, logo doanh nghiệp/tổ chức (đối với tổ chức). Dữ Liệu không bao gồm các thông tin không xác định danh tính của một Người Dùng cụ thể.
                    </p>
                    <h3>MỤC ĐÍCH THU THẬP THÔNG TIN VÀ PHẠM VI ÁP DỤNG</h3>
                    <p> Việc thu thập Dữ Liệu nhằm:
                    </p><p> - Tuân thủ các quy định pháp luật, thực hiện các nghĩa vụ pháp lý yêu cầu cung cấp hệ thống Dữ Liệu.
                    </p><p>- Thực hiện các quy trình, thủ tục nhằm đảm bảo mục đích kinh doanh của chúng tôi và quyền lợi của Người Dùng.
                    </p><p> - Đảm bảo việc lưu trữ hệ thống thông tin của Người Dùng trong hệ thống của MContract.
                    </p><p> - Tạo cơ sở để MContract giải quyết những vấn đề , tranh chấp (nếu có) xảy ra.

                    </p><p> Trong phạm vi về quyền thu thập thông tin của Người Dùng, MContract thu thập các Dữ Liệu khi Người Dùng truy cập hay sử dụng Dịch Vụ của chúng tôi, bao gồm những thông tin sau:
                    </p><p> - Thông tin đăng ký sử dụng: Khi Người Dùng đăng ký sử dụng Dịch Vụ của chúng tôi bằng cách tạo tài khoản, chúng tôi sẽ thu thập Dữ Liệu cần thiết để thực hiện Dịch Vụ được yêu cầu, bao gồm:  họ và tên, địa chỉ, số điện thoại, địa chỉ email, giới tính, ngày sinh, số chứng minh nhân dân hoặc căn cước công dân, ngày cấp, hình nhận diện Người Dùng, thông tin chữ ký số, logo doanh nghiệp/tổ chức (đối với tổ chức). Với các thông tin này, Người Dùng sẽ tự chịu trách nhiệm về bảo mật và lưu giữ mọi hoạt động sử dụng Dịch Vụ. MContract có thể yêu cầu bạn cung cấp Dữ Liệu bổ sung trong quá trình sử dụng Dịch Vụ.
                    </p><p> - Thông tin giao dịch và trải nghiệm:  Khi Người Dùng sử dụng các phương thức thanh toán cho Dịch Vụ của MContract, chúng tôi sẽ thu thập thông tin về các giao dịch này, cũng như thông tin khác liên quan đến giao dịch, bao gồm nhưng không giới hạn: số tiền thanh toán cho các Dịch Vụ, thông tin về tài khoản thanh toán, phương thức thanh toán được sử dụng để hoàn tất giao dịch, thông tin về thiết bị, dữ liệu sử dụng kỹ thuật và thông tin vị trí địa lý.
                    </p><p>- Thông tin bạn chọn cung cấp cho chúng tôi để có được Dịch Vụ bổ sung cụ thể: khi Người Dùng yêu cầu thêm các Dịch Vụ khác  hoặc các chức năng tùy chọn khác, chúng tôi có thể cần thu thập thêm thông tin.
                    </p><p>- Thông tin khác liên quan đến việc Người Dùng sử dụng các Dịch Vụ của MContract: khi Người Dùng liên lạc với MContract, liên hệ với bộ phận chăm sóc khách hàng của MContract hoặc trả lời khảo sát, chúng tôi có thể thu thập thêm thông tin cần thiết từ hoặc về Người Dùng.
                    </p>
                    <h3>PHƯƠNG THỨC THU THẬP</h3>
                    <p>Khi bạn truy cập, sử dụng Dịch vụ MContract có thể sử dụng Cookie và các công nghệ theo dõi khác (gọi chung là "Cookie") để nhận ra và tùy chỉnh trải nghiệm trực tuyến của Người Dùng: các Dịch Vụ được sử dụng, nội dung trực tuyến và quảng cáo; đo lường hiệu quả của chương trình khuyến mãi và thực hiện phân tích; và để giảm thiểu rủi ro, ngăn chặn gian lận tiềm ẩn và nâng cao độ tin cậy và an toàn trên Dịch Vụ của MContract.
                    </p><p>Để tránh nhầm lẫn, việc sử dụng Cookie chỉ được thực hiện dưới sự cho phép của Người Dùng. Tuy nhiên, lưu ý rằng, một số tính năng các Dịch Vụ của chúng tôi chỉ khả dụng khi sử dụng Cookie. Vì vậy, nếu bạn chọn tắt hoặc từ chối Cookie, việc sử dụng các Dịch Vụ của bạn có thể bị giới hạn hoặc không thể thực hiện được.
                    </p>
                    <h3>PHẠM VI SỬ DỤNG DỮ LIỆU</h3>
                    <p>MContract sử dụng các Dữ Liệu với sự chấp thuận của Người Dùng, nhằm thực hiện các mục đích được mô tả trong Chính Sách Bảo Mật này, hoặc khi MContract đánh giá việc này là cần thiết vì mục đích của lợi ích hợp pháp của các bên. Bao gồm:
                    </p><p> - Vận hành và cung cấp Dịch Vụ: Dữ Liệu thu thập được cho phép MContract thường xuyên liên lạc với Người Dùng về các Dịch Vụ mới nhất, cập nhật thông tin, sự kiện sắp tới của MContract. Để tránh nhầm lẫn, Người Dùng có quyền lựa chọn có/không tham gia vào danh sách gửi thư của MContract.
                    </p><p>- Gửi thông báo quan trọng liên quan đến Dịch Vụ: Dữ Liệu thu thập được cho phép MContract gửi các thông báo quan trọng như: thông tin về giá, các thay đổi đối với điều khoản, điều kiện và chính sách của MContract. Vì đây là thông tin quan trọng đối với hoạt động của các bên nên Người Dùng không thể từ chối nhận các thông báo này.
                    </p><p>- Cá nhân hóa trải nghiệm của Người Dùng: Dữ Liệu thu thập được cho phép MContract đề xuất, cung cấp cho Người Dùng các Dịch Vụ, lựa chọn, ưu đãi phù hợp.
                    </p><p>- Đáp ứng yêu cầu phát sinh tức thời của Người Dùng, ví dụ: để liên hệ với bạn về thắc mắc bạn đã gửi cho bộ phận chăm sóc khách hàng của chúng tôi.
                    </p><p> - Quản lý rủi ro và bảo vệ các Dịch Vụ và Người Dùng tránh khỏi gian lận bằng cách xác minh danh tính Người Dùng. Dữ Liệu thu thập được cùng với các thông tin thiết bị, dữ liệu kỹ thuật, vị trí địa lý giúp MContract phát hiện và ngăn chặn gian lận và lạm dụng các Dịch Vụ.
                    </p><p> - Quản lý, phát triển hoạt động kinh doanh: Dữ Liệu thu thập được cho phép MContract theo dõi, phân tích và cải thiện Dịch Vụ, quy trình chăm sóc khách hàng.
                    </p><p> - Thực hiện nghĩa vụ của chúng tôi với Chính Sách Bảo Mật này cũng như đối với tất cả các luật và quy định hiện hành.
                    </p>
                    <h3>CHIA SẺ DỮ LIỆU</h3>
                    <p>MContract cam kết không cung cấp, chia sẻ phát tán Dữ Liệu mà mình thu thập, quản lý cho bên thứ ba bất kỳ,  trừ trường hợp có sự đồng ý của Người Dùng đó.
                    </p><p>Trong một vài trường hợp, MContract có thể tiết lộ Dữ Liệu cho bên thứ ba, cụ thể như sau:
                    </p><p>- Khi có sự đồng ý của Người Dùng.
                    </p><p>- Đáp ứng yêu cầu của Người Dùng, bao gồm nhưng không giới hạn việc liên kết thanh toán, giao nhận hợp đồng, chứng từ liên quan đến Dịch Vụ theo yêu cầu của Người Dùng. Trong những trường hợp này, chúng tôi yêu cầu các bên thứ ba đó phải đảm bảo xử lý các Dữ Liệu này theo luật pháp liên quan.
                    </p><p>- Theo pháp luật, quy trình pháp lý, kiện tụng và/hoặc có yêu cầu của cơ quan có thẩm quyền.
                    </p><p>- Trường hợp MContract tin rằng hoặc có lý do để tin rằng, việc tiết lộ Dữ Liệu là cần thiết hoặc phù hợp để phát hiện, ngăn chặn và xử lý hành vi gian lận, hành vi sử dụng trái phép Dịch Vụ, hành vi vi phạm các điều khoản hoặc chính sách của chúng tôi hay hoạt động phi pháp hoặc có hại khác; để bảo vệ chính chúng tôi (gồm quyền, tài sản, Dịch Vụ, ngăn chặn tổn thất tài chính hoặc thiệt hại vật chất), bạn hoặc những người khác; vì mục đích an ninh quốc gia, thực thi pháp luật hoặc các vấn đề khác.
                    </p><p>- Ngoài ra, trong trường hợp tổ chức lại, sáp nhập, MContract có thể chuyển giao bất kỳ và tất cả Dữ Liệu chúng tôi thu thập được cho bên thứ ba có liên quan.
                    </p>
                    <h3>THỜI GIAN LƯU TRỮ DỮ LIỆU</h3>
                    <p>Việc lưu trữ Dữ liệu sẽ được bảo mật, lưu trữ tại máy chủ của MContract là vĩnh viễn.
                    </p>
                    <h3>PHƯƠNG TIỆN VÀ CÔNG CỤ ĐỂ NGƯỜI DÙNG TIẾP CẬN VÀ CHỈNH SỬA DỮ LIỆU</h3>
                    <p>Người Dùng có quyền tự kiểm tra, cập nhật, điều chỉnh thông tin cá nhân của mình bằng cách đăng nhập vào tài khoản và chỉnh sửa thông tin cá nhân hoặc yêu cầu MContract thực hiện việc này.
                    </p>
                    <h3>CAM KẾT BẢO MẬT DỮ LIỆU</h3>
                    <p>MContract luôn coi trọng việc bảo mật Dữ Liệu của Người Dùng. Chúng tôi duy trì các biện pháp bảo mật bằng kỹ thuật, vật lý và quản trị được thiết kế để cung cấp biện pháp bảo vệ hợp lý cho Dữ Liệu của Người Dùng khỏi bị thất lạc, lạm dụng, truy cập trái phép, tiết lộ và thay đổi.
                    </p><p>- Dữ Liệu của Người Dùng được MContract cam kết bảo mật tuyệt đối theo Chính Sách Bảo Mật này. Việc thu thập và sử dụng chỉ được thực hiện khi có sự đồng ý của Người Dùng hoặc theo yêu cầu của cơ quan nhà nước có thẩm quyền.
                    </p><p>- Không sử dụng, không chuyển giao, cung cấp hay tiết lộ cho bên thứ ba nào về Dữ Liệu khi không có sự cho phép từ Người Dùng.
                    </p><p>- Trong trường hợp máy chủ lưu trữ dữ liệu bị hacker tấn công dẫn đến mất mát Dữ Liệu, MContract sẽ có trách nhiệm thông báo vụ việc cho cơ quan chức năng điều tra xử lý kịp thời và thông báo cho Người Dùng được biết. MContract sẽ không phải chịu trách nhiệm phát sinh liên quan đến việc mất mát Dữ Liệu này của Người Dùng.
                    </p><p>- MContract yêu cầu các cá nhân khi đăng ký là thành viên, phải cung cấp đầy đủ thông tin cá nhân có liên quan như: họ và tên, địa chỉ liên lạc, email, số chứng minh nhân dân, số điện thoại …., và chịu trách nhiệm về tính pháp lý của những thông tin trên. MContract không chịu trách nhiệm cũng như không giải quyết mọi khiếu nại có liên quan đến quyền lợi của Người Dùng đó nếu xét thấy tất cả thông tin cung cấp khi đăng ký ban đầu là không chính xác.
                    </p><p>- Người Dùng có quyền gửi khiếu nại về việc lộ thông tin cá nhân cho bên thứ ba đến MContract. Khi tiếp nhận những phản hồi này, MContract sẽ xác nhận lại thông tin, phải có trách nhiệm trả lời lý do và hướng dẫn Người Dùng khôi phục và bảo mật lại thông tin.
                    </p><p>- Trường hợp MContract có sửa đổi, bổ sung thì không cần phải thông báo trước cho Người Dùng mà mặc định việc sửa đổi, bổ sung này. Người Dùng phải thường xuyên kiểm tra lại để có các bản cập nhật cho Chính Sách Bảo Mật này. Trong trường hợp MContract cập nhật Chính Sách Bảo Mật và Người Dùng tiếp tục sử dụng Dịch Vụ sau khi cập nhật, điều đó có nghĩa là Người Dùng đồng ý với (các) điều khoản mới được xác định trong bản cập nhật.
                    </p><p>- Mặc dù chúng tôi cam kết bảo vệ hệ thống và Dịch Vụ của chúng tôi nhưng Người Dùng vẫn có trách nhiệm bảo vệ và duy trì quyền riêng tư đối với mật khẩu và thông tin đăng ký tài khoản/hồ sơ của mình và xác minh rằng Dữ Liệu chúng tôi đang lưu giữ là chính xác và luôn được cập nhật.
                    </p><p>- Người Dùng tự chịu trách nhiệm đối với thông tin cá nhân khi chia sẻ về sản phẩm, Dịch Vụ của MContract trên các nền tảng.  Vui lòng cẩn thận khi chia sẻ các thông tin này.
                    </p>
                </div>
            </SimpleBar>
        </div>
    );
}

export { PageSecurity };