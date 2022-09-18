import Link from "next/link";
import Layout from "@/components/Layout";
import Head from "next/head";
import { useState, useEffect } from 'react';
import Footer from "@/components/Footer";

const Policy = () => {
    const [showResults, setShowResults] = useState(true);
    useEffect(() => {
        window.top;
    });
    return (
        <Layout>
            <Head>
                <title>MContract</title>
            </Head>
            {showResults ? <>
                <div className="info pt-4 pb-2 intro-extra">
                    <div className="container">
                        <div className="row info" data-aos="fade-up">
                            <div className="col-12 col-lg-7">
                                <div className="text_info">
                                    <div className="title mb-2">
                                        <p className="content_vi">Điều khoản và điều kiện</p>
                                        <h2 className="content_vi">Khi khách hàng sử dụng hệ thống MContract</h2>
                                    </div>
                                    <p className="content_vi">
                                        Người Sử Dụng cần đọc và đồng ý với những Điều Khoản Và Điều Kiện này trước khi sử dụng Dịch Vụ.
                                        Điều Khoản Và Điều Kiện điều chỉnh các quyền và nghĩa vụ của Người Dùng, với tư cách là khách hàng, khi sử dụng Dịch Vụ do MContract cung cấp.
                                    </p>
                                </div>
                            </div>
                            <div className="col-12 col-lg">
                                <div className="img_info mt-3 mt-lg-0 text-center">
                                    <img className="img-fluid" src="/images/intro/policy.svg" alt="" loading="lazy" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="language">
                    <button type="button" id="en" className="btn btn-primary" onClick={() => setShowResults(false)}>
                        <img src="/images/english.webp" className="rounded-circle" alt="" loading="lazy" /> <span className="name_language">English</span>
                    </button>
                </div>
                <div className="container content intro-extra">
                    <div className="content_vi">
                        <h3>ĐỊNH NGHĨA</h3>
                        <p>
                            Các thuật ngữ viết hoa trong Điều Khoản Và Điều Kiện này có ý nghĩa như sau:
                        </p>
                        <p>
                            <b><i>“MContract”</i></b> nghĩa là Công Ty TNHH Dịch Vụ Công Nghệ Trực Tuyến. Trong Điều khoản Và Điều Kiện này, MContract có thể  được đề cập là “Chúng Tôi” tùy theo ngữ cảnh.
                        </p>
                        <p>
                            <b><i>“Dịch Vụ”</i></b> nghĩa là mọi sản phẩm, Dịch Vụ, nội dung, tính năng, công nghệ hay chức năng, cũng như tất cả các trang web, ứng dụng liên quan do MContract cung cấp cho Người Dùng.
                        </p>
                        <p>
                            <b><i>“Người Dùng”</i></b> nghĩa là cá nhân/tổ chức sử dụng Dịch Vụ.
                        </p>
                        <p><b><i>“Dữ Liệu”</i></b> có thể bao gồm họ và tên, địa chỉ, số điện thoại, địa chỉ email, giới tính, ngày sinh, số chứng minh nhân dân hoặc căn cước công dân, ngày cấp, hình nhận diện Người Dùng, thông tin chữ ký số, logo doanh nghiệp/tổ chức (đối với tổ chức). Dữ Liệu không bao gồm các thông tin không xác định danh tính của một Người Dùng cụ thể.
                        </p>
                        <h3> TÀI KHOẢN NGƯỜI DÙNG</h3>
                        <p>
                            - Một số tính năng, Dịch Vụ yêu cầu Người Dùng phải tạo tài khoản và được cấp phép từ MContract. Người Dùng hoàn toàn chịu trách nhiệm duy trì, cập nhật chính xác thông tin cũng như tính bảo mật của dữ liệu cá nhân, bao gồm nhưng không giới hạn mọi hoạt động xảy ra trong tài khoản. Người Dùng đồng ý thông báo cho MContract ngay lập tức về bất kỳ vi phạm nào đối với tài khoản hoặc bất kỳ vi phạm bảo mật nào khác.
                        </p><p>- Người Dùng có thể phải chịu trách nhiệm cho những mất mát do MContract hoặc bất kỳ Người Dùng khác hoặc khách truy cập vào Dịch Vụ do những đối tượng này sử dụng thông tin tài khoản của bạn xuất phát từ lỗi không bảo mật và bảo mật thông tin tài khoản của mình.
                        </p><p>  - Chấm dứt tài khoản: Người Dùng có thể đóng tài khoản và chấm dứt mối quan hệ với MContract bất cứ lúc nào mà không bị mất phí hoặc tiền phạt. Tuy nhiên, Người Dùng vẫn phải chịu trách nhiệm với các quyền và nghĩa vụ liên quan đến tài khoản ngay cả khi tài khoản đã chấm dứt.
                        </p><p>  - Tài khoản Người Dùng có thể chấm dứt trong các trường hợp sau đây:
                        </p><p className="plus">  + Người Dùng thông báo và đề nghị việc chấm dứt tài khoản với MContract.
                        </p><p className="plus"> + Người Dùng vi phạm các thỏa thuận pháp lý của MContract và/hoặc các quy định pháp luật khác.
                        </p><p> - Bất kỳ trường hợp chấm dứt tài khoản nào cũng sẽ được MContract gửi thông báo đến Người Dùng với lý do cụ thể.
                        </p><p>  - Tài khoản Người Dùng không thể chấm dứt trong các trường hợp sau đây:
                        </p><p className="plus"> + Người Dùng đang có giao dịch, nghĩa vụ chưa hoàn thành và cần xử lý.
                        </p><p className="plus"> + Tài khoản Người Dùng đang có tranh chấp, khiếu nại hoặc các vấn đề cần chờ xác minh.
                        </p>
                        <h3> NỘI DUNG VÀ PHẠM VI SỬ DỤNG</h3>
                        <p> - Người Dùng đồng ý KHÔNG thực hiện các hành vi sau đây trong quá trình sử dụng Dịch Vụ của MContract:
                        </p><p className="plus"> + Xâm nhập, tìm cách xâm nhập trái phép vào tài khoản Người Dùng khác để truy cập và sử dụng Dịch Vụ.
                        </p><p className="plus">  + Xâm nhập, tìm cách xâm nhập, tiếp cận hoặc sử dụng bất kỳ các Dữ Liệu nào trên Dịch Vụ và máy chủ của MContract mà không được cho phép.
                        </p><p className="plus">  + Phát tán, sử dụng các chương trình có hại gây trở ngại, gian lận hoặc ảnh hưởng tới hệ thống, Dữ Liệu, thông tin, Dịch Vụ của MContract.
                        </p><p className="plus"> + Thực hiện các hành vi làm tổn hại đến uy tín của MContract và/hoặc các Dịch Vụ của MContract dưới bất kỳ hình thức nào.
                        </p><p className="plus">  + Thực hiện các hành vi  trái với thuần phong mỹ tục, đạo đức xã hội và quy định pháp luật dưới bất kỳ hình thức nào.
                        </p><p className="plus"> + Sử dụng các thông tin, Dữ Liệu vào mục đích thương mại.
                        </p>
                        <h3>ĐIỀU KHOẢN THANH TOÁN</h3>
                        <p> - Thanh toán chung:
                        </p><p className="plus"> + Người Dùng có trách nhiệm kiểm tra kỹ Dịch Vụ, thỏa thuận Dịch Vụ, điều khoản sử dụng trước khi thực hiện thanh toán.
                        </p><p className="plus"> + Chi phí các Dịch Vụ do MContract cung cấp  đã bao gồm thuế VAT.
                        </p><p className="plus">  + MContract có quyền cập nhật và thay đổi giá Dịch Vụ tùy từng thời điểm và mục đích kinh doanh. Việc thay đổi mức giá được cập nhật công khai và gửi thông báo đến Người Dùng trong thời gian hợp lý. Mức giá mới có hiệu lực vào thời điểm được cập nhật tại trang web mcontract.vn. Nếu bạn tiếp tục sử dụng hoặc đăng ký sử dụng Dịch Vụ  sau thời điểm thay đổi có nghĩa là bạn đã đồng ý chấp nhận mức giá mới.
                        </p><p className="plus"> + MContract không có trách nhiệm hoàn trả chi phí Dịch Vụ và hoặc chịu bất kỳ chi phí nào khác sau khi Người Dùng đã thanh toán, bao gồm nhưng không giới hạn các trường hợp sử dụng một phần Dịch Vụ, đề nghị chấm dứt Dịch Vụ trước thời hạn.
                        </p><p className="plus"> + Liên kết thanh toán:  MContract sử dụng liên kết thanh toán với bên thứ ba nhằm đảm bảo mục đích kinh doanh và đáp ứng nhu cầu sử dụng của Người Dùng, bao gồm nhưng không giới hạn tiền mặt, Internet Banking, ví điện tử,... Trong trường hợp này, chúng tôi yêu cầu các bên thứ ba đó phải đảm bảo các nghĩa vụ tương ứng với MContract theo luật pháp liên quan.
                        </p><p>  - Đối với trường hợp phát sinh lỗi khi thanh toán, Người Dùng có nghĩa vụ thông báo và cung cấp các chứng từ xác minh cho giao dịch thanh toán cho MContract để được kiểm tra và xử lý. Chúng tôi chỉ chịu trách nhiệm đối với các trường hợp lỗi phát sinh từ phía MContract.  MContract không có chính sách hoàn trả lại giao dịch đã thực hiện.
                        </p><p> - MContract luôn ưu tiên và nỗ lực xây dựng một cơ chế thanh toán an toàn cho các giao dịch  nhưng vẫn đáp ứng được yêu cầu sử dụng của Người Dùng.
                        </p>
                        <h3>QUYỀN VÀ NGHĨA VỤ NGƯỜI DÙNG</h3>
                        <p>Trong phạm vi thỏa thuận này, Người Dùng có các quyền và nghĩa vụ sau:
                        </p><p> - Được cung cấp, giới thiệu đầy đủ, phù hợp các Dịch Vụ nhằm đáp ứng nhu cầu sử dụng.  Theo đó, việc Người Dùng cam kết đảm bảo thực hiện đúng các thỏa thuận pháp lý của chúng tôi sẽ là cơ sở để đảm bảo quyền này.
                        </p><p> - Nhận được sự hỗ trợ từ các kênh chăm sóc khách hàng của MContract. Theo đó, việc Người Dùng phải cam kết cung cấp thông tin đầy đủ,  trung thực, chính xác và luôn cập nhật sẽ là cơ sở để chúng tôi đảm bảo quyền này. Người Dùng phải chịu trách nhiệm về toàn bộ các thông tin cung cấp.
                        </p><p>  - Người Dùng có trách nhiệm bảo mật dữ liệu cá nhân như: số điện thoại, thông tin giấy tờ tùy thân, email bảo vệ tài khoản, thông tin chứng thư số, ... Nếu những thông tin trên bị tiết lộ dưới bất kỳ hình thức nào thì Người Dùng phải chấp nhận những rủi ro phát sinh. Người Dùng đồng ý sẽ thông báo ngay lập tức cho MContract  về bất kỳ trường hợp nào sử dụng trái phép tài khoản và mật khẩu của bạn hoặc bất kỳ các hành động phá vỡ hệ thống bảo mật nào.
                        </p><p>  - Cam kết không thực hiện các hành vi được quy định tại thỏa thuận này.
                        </p><p>  - Thông báo cho MContract theo các kênh liên lạc được cung cấp tại thỏa thuận này khi phát hiện ra lỗi của Dịch Vụ, các vấn đề gây ảnh hưởng tới hoạt động bình thường của Dịch Vụ liên quan.Thực hiện trách nhiệm khác theo quy định pháp luật.
                        </p>
                        <h3>QUYỀN VÀ NGHĨA VỤ CỦA MContract</h3>
                        <p> Trong phạm vi thỏa thuận này, MContract có các quyền và nghĩa vụ sau:
                        </p><p>- Đảm bảo tính pháp lý của Dịch Vụ cung cấp cho Người Dùng. Theo đó, MContract cam kết công khai các Dịch Vụ, Chi phí Dịch Vụ, thỏa thuận cũng như các vấn đề liên quan đến Người Dùng.
                        </p><p>- Hỗ trợ Người Dùng trong quá trình sử dụng Dịch Vụ được cung cấp; Tiếp nhận, giải quyết khiếu nại đúng quy định của Người Dùng trong quá trình sử dụng Dịch Vụ được cung cấp.
                        </p><p> - Bảo mật Dữ Liệu của Người Dùng. MContract cam kết không bán hoặc trao đổi những thông tin này với bên thứ ba, trừ trường hợp theo quy định pháp luật hoặc được thành viên chấp nhận.
                        </p><p>   - Rà soát và có quyền tạm khóa, chấm dứt tài khoản của Người Dùng mà không cần sự đồng ý và không phải chịu bất cứ trách nhiệm nào trong trường hợp phát hiện hoặc có cơ sở cho thấy Người Dùng cung cấp thông tin không trung thực, không chính xác, hoặc  Người Dùng vi phạm bất cứ điều khoản nào trong thỏa thuận pháp lý của chúng tôi.
                        </p><p> - Khi phát hiện những vi phạm như gian lận, bẻ khóa hay đánh cắp thông tin hoặc những lỗi khác, ở mức độ hợp lý, MContract có quyền yêu cầu cơ quan chức năng can thiệp để giải quyết. Theo đó, chúng tôi có quyền sử dụng thông tin được cung cấp để phục vụ quá trình này  theo quy định của pháp luật.
                        </p><p>   - Phối hợp với cơ quan nhà nước có thẩm quyền trong các trường hợp cần thiết liên quan quy trình báo cáo, thanh tra, kiểm tra, kiện tụng pháp lý; liên quan đến các hoạt động khủng bố, tội phạm, an ninh quốc gia.
                        </p>
                        <h3> BẢO MẬT DỮ LIỆU CÁ NHÂN</h3>
                        <p> Tại MContract việc bảo vệ Dữ Liệu của Người Dùng luôn là ưu tiên hàng đầu. Chúng tôi hiểu rằng, các dữ liệu của Người Dùng là thuộc quyền sở hữu hợp pháp và được pháp luật bảo vệ, chính vì vậy, việc thu thập, lưu giữ, xử lý Dữ Liệu chỉ được MContract thực hiện khi có sự đồng ý và cho phép của Người Dùng.
                        </p><p>  Chúng tôi luôn nỗ lực xây dựng và hoàn thiện các điều khoản trong Chính Sách Bảo Mật để đảm bảo các tiêu chuẩn và yêu cầu của Người Dùng. Theo đó, Chính Sách Bảo Mật áp dụng cho toàn bộ việc sử dụng Dịch Vụ của MContract. Các điều khoản của Chính Sách Bảo Mật cũng được coi là một phần của thỏa thuận người dùng này. Bạn vui lòng tìm hiểu chi tiết tại “Chính Sách Bảo Mật”.
                        </p>
                        <h3>TRƯỜNG HỢP BẤT KHẢ KHÁNG</h3>
                        <p>Nếu phát sinh rủi ro, thiệt hại trong trường hợp bất khả kháng bao gồm nhưng không giới hạn như chập điện, hư hỏng phần cứng, phần mềm, sự cố đường truyền internet hoặc do thiên tai, .... Người Dùng phải chấp nhận những rủi ro, thiệt hại nếu có. MContract cam kết sẽ nỗ lực giảm thiểu những rủi ro, thiệt hại phát sinh. Tuy nhiên chúng tôi sẽ không chịu bất cứ trách nhiệm nào phát sinh trong các trường hợp này.
                        </p>
                        <h3>SỞ HỮU TRÍ TUỆ</h3>
                        <p>- Người Dùng đồng ý rằng Dịch vụ, bao gồm nhưng không giới hạn ở nội dung, đồ họa, giao diện Người Dùng, hình ảnh, logo, tài liệu điện tử, clip âm thanh, clip video, nội dung biên tập và phần mềm được sử dụng để thực hiện Dịch Vụ, có chứa thông tin và tài liệu độc quyền thuộc sở hữu của MContract và/hoặc bên cấp phép của MContract, và được bảo vệ theo luật sở hữu trí tuệ và các luật khác, bao gồm nhưng không giới hạn ở luật bản quyền. Người Dùng đồng ý sẽ không sử dụng thông tin hoặc tài liệu độc quyền này theo bất kỳ cách nào, ngoại trừ việc sử dụng Dịch Vụ phù hợp với Thỏa thuận này.
                        </p><p>- Tất cả quyền sở hữu trí tuệ tồn tại trên, trong các Dịch Vụ đều thuộc về MContract hoặc được cấp phép hợp pháp cho MContract. Theo đó, tất cả các quyền hợp pháp của MContract đều được đảm bảo bởi pháp luật. Trừ khi có sự đồng ý bằng văn bản của MContract, Người Dùng không được phép sử dụng, sao chép, xuất bản, tái sản xuất, truyền hoặc phân phát bằng bất cứ hình thức nào, bất cứ thành phần nào của Dịch Vụ.
                        </p><p>- MContract có toàn quyền, bao gồm nhưng không giới hạn các quyền về thương hiệu, bí mật kinh doanh và các quyền sở hữu khác đối với Dịch Vụ của MContract. Việc sử dụng quyền và sở hữu của MContract cần phải được cấp phép rõ ràng bằng văn bản. Theo đó chúng tôi không cấp phép dưới bất kỳ hình thức nào bao gồm nhưng không giới hạn các công bố, hàm ý cho phép Người Dùng thực hiện các quyền trên.
                        </p>
                        <h3>CHẤM DỨT VÀ TẠM NGỪNG DỊCH VỤ</h3>
                        <p>MContract có quyền ngừng cung cấp Dịch Vụ cho Người Dùng trong các trường hợp sau: Người Dùng không tuân thủ, hoặc có căn cứ để MContract nghi ngờ Người Dùng đã không tuân thủ, bất kỳ điều khoản nào của thỏa thuận này; Người Dùng sử dụng Dịch Vụ và/hoặc trang web nhằm thực hiện hành vi vi phạm pháp luật. Bao gồm nhưng không giới hạn các hành vi gian lận trong sử dụng Dịch Vụ, gian lận trong thanh toán…; Theo yêu cầu của cơ quan chức năng có thẩm quyền và quy định của pháp luật.
                        </p>
                        <p>Theo đó MContract có thể, mà không cần thông báo cho quý khách: (i) chấm dứt Thỏa thuận này và/hoặc tài khoản của Người Dùng và/hoặc (ii) chấm dứt thỏa thuận sử dụng Dịch Vụ của quý khách; và/hoặc (iii) ngăn cản quý khách truy cập vào trang web.</p>

                        <h3>TỪ CHỐI BẢO ĐẢM VÀ GIỚI HẠN TRÁCH NHIỆM</h3>
                        <p> - MContract tuyên bố từ chối trách nhiệm đối với mọi thiệt hại, trách nhiệm pháp lý phát sinh do bất kỳ lỗi hệ thống, lỗi đường truyền; sự cố kỹ thuật; sự cố máy chủ hoặc do trường hợp Bất khả kháng; … Người Dùng phải chấp nhận những rủi ro, thiệt hại nếu có. Tuy nhiên, MContract cam kết sẽ nỗ lực giảm thiểu rủi ro, thiệt hại phát sinh.
                        </p><p> - MContract sẽ không chịu bất kỳ loại trách nhiệm nào cùng với các khoản thanh toán, phí, tiền phạt hay bồi thường mà không phải phát sinh từ lỗi cố ý của MContract.
                        </p><p> - MContract sẽ thực hiện những cố gắng hợp lý để bảo vệ thông tin do Người Dùng cung cấp có liên quan đến Dịch Vụ. Tuy nhiên, Người Dùng đồng ý miễn trừ MContract đối với bất kỳ và tất cả trách nhiệm pháp lý với bất kỳ thiệt hại hoặc trách nhiệm nào liên quan đến các thông tin đó.
                        </p><p> - Người Dùng đồng ý bồi thường và giữ cho MContract, giám đốc, thành viên sáng lập, người tiền nhiệm, người kế thừa, nhân viên, đại lý, công ty con và chi nhánh, không bị tổn hại bởi bất kỳ yêu cầu, trách nhiệm, khiếu nại hoặc chi phí nào (kể cả phí luật sư) bởi bất kỳ bên thứ ba nào do hoặc phát sinh từ hoặc liên quan đến việc Người Dùng sử dụng Dịch Vụ.
                        </p>
                        <h3>GIẢI QUYẾT TRANH CHẤP</h3>
                        <p> - Tất cả các vấn đề liên quan đến việc Người Dùng truy cập hoặc sử dụng Dịch Vụ, bao gồm các tranh chấp, sẽ chịu sự điều chỉnh của luật pháp Việt Nam.
                        </p><p> - Các khiếu nại phát sinh trong quá trình sử dụng Dịch Vụ phải được gửi đến MContract ngay khi sự kiện đó phát sinh. Chúng tôi cam kết sẽ hỗ trợ giải quyết hợp lý cho những trường hợp cung cấp thông tin đầy đủ chính xác. Bên khiếu nại có nghĩa vụ cung cấp bằng chứng liên quan đến khiếu nại và chịu trách nhiệm về nội dung khiếu nại cũng như bằng chứng đã cung cấp.
                        </p><p> - Trong trường hợp xảy ra tranh chấp giữa Người Dùng hoặc tranh chấp với bên thứ ba, các bên  sẽ tự sắp xếp giải quyết vấn đề của mình. MContract sẽ hỗ trợ thông tin liên hệ và các thông tin hợp lý để bảo vệ tối đa quyền lợi hợp pháp và chính đáng của Người Dùng.
                        </p>
                        <h3>HIỆU LỰC THỎA THUẬN</h3>
                        <p>- Nếu bất kỳ phần nào của thỏa thuận này không có hiệu lực hoặc không thể thi hành, phần đó sẽ được hiểu theo cách phù hợp với pháp luật và tiếp tục có hiệu lực thi hành. Việc MContract không thực hiện bất kỳ quyền hoặc quy định nào trong thỏa thuận này sẽ không được coi là từ bỏ quy định đó hoặc bất kỳ quy định nào khác. Chúng tôi cũng sẽ không chịu trách nhiệm về việc không thực hiện bất kỳ nghĩa vụ nào do các nguyên nhân nằm ngoài tầm kiểm soát của mình.
                        </p><p>- Người Dùng đồng ý cấp cho MContract quyền thực hiện các bước cần thiết hoặc thích hợp một cách hợp lý để thi hành và/hoặc xác minh sự tuân thủ với bất kỳ phần nào của thỏa thuận này.
                        </p>
                    </div>
                </div>
            </> : <>
                <div className="info pt-4 pb-2 intro-extra">
                    <div className="container">
                        <div className="row info" data-aos="fade-up">
                            <div className="col-12 col-lg-7">
                                <div className="text_info">
                                    <div className="title mb-2">
                                        <p className="content_en">TERMS AND CONDITIONS</p>
                                        <h2 className="content_en">When customers use MContract system</h2>
                                    </div>
                                    <p className="content_en">
                                        Users are required to read and agree to these Terms and Conditions before using the Service.
                                        The Terms and Conditions govern the rights and obligations of the User, as a customer, to use the Services provided by MContract.
                                    </p>
                                </div>
                            </div>
                            <div className="col-12 col-lg">
                                <div className="img_info mt-3 mt-lg-0 text-center">
                                    <img className="img-fluid" src="/images/intro/policy.svg" alt="" loading="lazy" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="language">
                    <button type="button" id="vi" className="btn btn-primary" onClick={() => setShowResults(true)}>
                        <img src="/images/vietnam.webp" className="rounded-circle" loading="lazy" /> <span className="name_language">Tiếng Việt</span>
                    </button>
                </div>
                <div className="container content intro-extra">
                    <div className="content_en">

                        <h3>DEFINITION/GLOSSARY</h3>
                        <p>
                            <b><i> “MContract”</i></b>, also know as "ONLINE TECHNOLOGY SERVICES COMPANY LIMITED". In this Terms and Conditions, MContract may be referred to as "We" depending on the context.
                        </p>
                        <p>
                            <b><i>“Service”</i></b> means any product, Service, content, feature, technology, or functionality, as well as all related webpages and relevant applications made available to Users by MContract.
                        </p>
                        <p>
                            <b><i>“User”</i></b> means the person/organization that uses the Service.
                        </p>
                        <p>
                            <b><i>“Data”</i></b> may include first name and last name, address, phone number, email address, gender, date of birth, identity card number or identification number, date of issue, User photo ID, digital signature information, corporate/organization logo (for organizations). Data does not include information that does not identify a particular User.
                        </p>

                        <h3> USER ACCOUNT</h3>
                        <p> - Some features, Services require the User to create an account and be licensed from MContract. The User is solely responsible for maintaining, updating correct information of their own, as well as the confidentiality of personal Data, including but not limited to all activities that occur in the account. The User agrees to notify MContract immediately of any breach of the account or any other breach of the security.
                        </p><p> - Users may be liable for the losses generated from MContract or any other Users or visitors to the Service due to another person using your account information without your consent, resulting from the lack of your own security and confidential security of your account information.
                        </p><p> - Account termination: The User can close the account and terminate the relationship with MContract at any time without loss of fees or penalties. However, the User is still responsible for rights and obligations related to the account even after the account has been terminated.
                        </p><p> - The User Account may be terminated in the following cases:
                        </p><p className="plus">  + The User notices and requests for the termination of the account with MContract.
                        </p><p className="plus"> + The User violates MContract's legal agreements and/or other laws.
                        </p><p>  - Any account termination will be notified by MContract to the User for the specific reason(s).
                        </p><p> - The User Account cannot be terminated in the following cases:
                        </p><p className="plus">   + User has transactions, obligations that are not completed and need to be processed.
                        </p><p className="plus">  + User Account has disputes, complaints, or issues that need further verification.
                        </p>

                        <h3> CONTENT AND SCOPE OF USE</h3>
                        <p>  - The User agrees NOT to perform the following actions in the course of using the MContract Service:
                        </p><p className="plus">  + Trespassing, attempting to illegally break into another User's account to access and use the Service.
                        </p><p className="plus">  + Infiltrate, attempt to enter, access, or use any Data on the Services and servers of MContract without permission.
                        </p><p className="plus">  + Distributing and using harmful programs that interfere, cheat or damage the systems, Data, information, Services of MContract.
                        </p><p className="plus">   + Performing actions that damage the reputation of MContract and/or MContract Services in any way.
                        </p><p className="plus">  + Committing actions that are contrary to general Vietnamese customs and practices, social ethics, and legal regulations in any form.
                        </p><p className="plus">   + Using information and Data for commercial purposes.
                        </p>

                        <h3>    TERMS OF PAYMENT</h3>
                        <p>  - General payment:
                        </p><p className="plus"> + User is responsible for checking the Service, the Service agreement, terms of use carefully before making a payment.
                        </p><p className="plus">  + The cost of Services provided by MContract includes VAT.
                        </p><p className="plus">  + MContract reserves the right to update and change the price of the Service from time to time and for business purposes. Rate changes are publicly updated and notified to the User within a reasonable time. The new price will take effect at the time it is updated on mcontract.vn website. If you continue to use or register to use the Service after the time of change, you agree to accept the new price.
                        </p><p className="plus">   + MContract is not responsible to refund the cost of the Service and or bear any other costs after the User has paid, including but not limited to partial use of the Service, request to terminate the Service ahead of due time.
                        </p><p className="plus">   + Payment link: MContract uses payment links with third parties to ensure business purposes and meet the User needs, including but not limited to cash, Internet Banking, e-wallet, ... In this case, we require such third parties to commit to their respective obligations with MContract under the relevant laws.
                        </p><p>  - In case of payment error, the User is obliged to notify and provide verification documents for payment transaction to MContract to be checked and processed. We are only responsible for errors arising from the MContract side. MContract does not have the policy to return the amount in the transactions that have been made.
                        </p><p>  - MContract always prioritizes and tries to build a safe payment system for transactions while meeting the User’s requirements.
                        </p>

                        <h3> USER RIGHTS AND OBLIGATIONS</h3>
                        <p>  Within the scope of this agreement, the User has the following rights and obligations:
                        </p><p>  - Be provided, fully and suitably informed of the Services to use them. Accordingly, the User's commitment to comply with our legal agreements is fundamental to cover this right.
                        </p><p>   - Receive support from customer care channels of MContract. Accordingly, the User's commitment to providing complete, truthful, accurate, and up-to-date information is fundamental to cover this right. The Users are responsible for all information they provided.
                        </p><p>   - The Users are responsible for protecting their own personal Data such as phone number, ID information, email attached to the account, digital certificate information, ... If the above information is disclosed under any form, the User must accept risks associating with the action. The Users agree to immediately notify MContract of any unauthorized use of their account and password or any action of circumventing the security system.
                        </p><p>   - Commit to not perform the actions specified in this agreement.
                        </p><p>  - Notify MContract via the communication channels provided in this agreement when they detect errors of the Service, problems affecting the normal operation of the relevant Service. Perform other responsibilities per the law.
                        </p>

                        <h3>RIGHTS AND OBLIGATIONS OF MContract</h3>
                        <p>  Within the scope of this agreement, MContract has the following rights and obligations:
                        </p><p>  - Affirm the legality of the Service provided to the User. Accordingly, MContract commits to publicizing Services, Service fees and agreements, and issues related to the Users.
                        </p><p> - Assist the Users in using the Service; Receive and resolve complaints per the Term and Conditions during the use of the Service provided.
                        </p><p>  - Secure User’s Data. MContract commits to not sell or exchange User information with third parties, except in the cases required by the law or by that User’s acceptance.
                        </p><p>  - Review and retain the right to temporarily lock, terminate the User's account without consent and without any responsibility in case of detecting or having ground evidence showing that the User provides the information dishonestly, inaccurate, or the User violates any of the terms of our legal agreement.
                        </p><p>  - Upon detecting violations such as fraud, cracking or stealing information, or other errors, to a reasonable extent, MContract has the right to request the authorities to intervene to resolve. Accordingly, we have the right to use the information provided to the Service to support the law enforcement process.
                        </p><p> - Coordinate with competent state agencies in necessary cases related to the reporting, inspection, examination, and legal proceedings; related to terrorist activities, crime, national security.
                        </p>

                        <h3> PERSONAL DATA SECURITY</h3>
                        <p>  At MContract, protecting the User's personal Data is the main priority. We understand that the User's Data legally belongs to the User and is protected by law. Therefore, MContract only collects, stores, and processes the User’s Data under the consent and permission of the User.
                        </p><p> We endeavor to always develop and polish the terms in the Privacy Policy to secure the standards and requirements for our Users. Accordingly, the Privacy Policy applies to all the uses of the Service by MContract. The terms in the Privacy Policy are also considered a part of this User agreement. Please find out more details in the "Privacy Policy" document.
                        </p>

                        <h3> FORCE MAJEURE CLAUSE</h3>
                        <p> If risks and damages occur in a force majeure case, including but are not limited to electrical short, damage to hardware & software, internet connection failure or natural disasters, etc, the Users must accept the risks and damages that occurred. MContract commits to make the very best effort to minimize the risks and losses that occurred, however; we will not bear any responsibility regarding these cases.
                        </p>
                        <h3> INTELLECTUAL PROPERTY</h3>
                        <p>  - The User agrees that the Service, including but not limited to content, graphics, User interfaces, images, logos, electronic documents, sound clips, video clips, editorial content, and software is used to perform the Services, contains proprietary information and materials owned by MContract and or its licensors, and all the proprietary information and materials mentioned above are protected under Intellectual Property Law and other laws, including but not limited to copyright laws. The User agrees to not use this proprietary information or material in any way, except to use of Service by this Agreement.
                        </p><p>   - All intellectual property rights mentioned above, in the Services belong exclusively to MContract or are legally licensed to MContract. Accordingly, all legal rights of MContract are guaranteed by law. Unless otherwise agreed in writing by MContract, Users may not use, copy, publish, reproduce, transmit or distribute in any form, any component of the Service.
                        </p><p>   - MContract has all rights, including but not limited to, trademark rights, trade secrets, and other proprietary rights to MContract Services. The use of MContract's rights and ownership must be explicitly authorized in the writing forms. Accordingly, we do not grant any license of any kind, including but not limited to claims, implying a license for the User to exercise such rights.
                        </p>
                        <h3>TERMINATION AND  TEMPORARY TERMINATION OF SERVICE</h3>
                        <p>  MContract reserves the right to discontinue the Service to the User in the following cases: The User fails to comply, or there is ground evidence for MContract to suspect that the User has failed to comply with any of the terms in this agreement; The User uses the Service and or the website to commit illegal actions including but not limited to frauds in using the service, fraud in payment ..etc; At the request from competent authorities and in accordance with the effective laws in Vietnam.
                        </p><p>   Accordingly, MContract may, without prior notice to you: (i) terminate this Agreement and/or the User's account and/or (ii) terminate your agreement to use the Services; and/or (iii) prevent you from accessing the website. The User uses the Service and/or the website to commit a law violation, including but not limited to, fraudulent practices in the Service usage, payment fraud ... As required by competent authorities and regulations of law.
                        </p>

                        <h3>  DISCLAIMER AND LIMITATION OF LIABILITY</h3>
                        <p> - MContract disclaims liability for any damages or liabilities arising from any system failure, transmission error; technical problems; server failure, or Force Majeure ... Users must accept the risks and damages that occur. However, MContract commits to minimizing risks and potential losses.
                        </p><p>  - MContract will not bear any type of liability regarding payments, fees, penalties, or compensation that does not occur from the side of MContract.
                        </p><p>   - MContract will take reasonable efforts to protect the information provided by the User in connection with the Service. However, the User agrees to waive MContract from any and all liability for any damages or liabilities related to such information.
                        </p><p>  - User agrees to indemnify and keep MContract, director, founding member, predecessor, heir, employee, agent, subsidiary, and an affiliate, from any harm from any of such request, liability, claim, an or expense (including attorneys' fees) by any third party resulting from, or arising out of, or in connection with the User's use of the Service.
                        </p>

                        <h3>  DISPUTE RESOLUTION</h3>
                        <p>   - All matters related to the User's access or the use of the Service, including disputes, will be governed by effective laws in Vietnam.
                        </p><p>   - Claims arising during the use of the Service must be sent to MContract as soon as the event emerges. We are committed to providing full support and reasonable settlement for cases where information is provided completely and accurately. The complainant is obliged to provide evidence related to the complaint and is responsible for the content of the complaint and the evidence provided.
                        </p><p>   - In the event of a dispute between the User or a dispute between the User and a third party, the parties will arrange to resolve the matter by themselves. MContract will provide reasonable support and communication to maximize the protection of legal and legitimate rights of the User.
                        </p>

                        <h3> EFFECT OF AGREEMENT</h3>
                        <p>  - If any part of this agreement is invalid or unenforceable, that part will be construed in a manner consistent with the law and continue to be enforceable. MContract's failure to exercise any right or provision of this agreement will not constitute a waiver of that or any other provision. We also will not be responsible for failure to perform any obligations due to reasons beyond our control.
                        </p><p>   - The User agrees to grant MContract the right to take necessary or appropriate steps reasonably to enforce and/or verify compliance with any part of this agreement.
                        </p>
                    </div>
                </div>
            </>
            }
            <Footer page="security" />
        </Layout>
    )
}
export default Policy