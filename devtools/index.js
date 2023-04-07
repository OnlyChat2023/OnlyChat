import axios from 'axios';
import slugify from 'slugify';
import cheerio from 'cheerio';
import fs from 'fs';

const mientay = ['An Giang', 'Bạc Liêu', 'Bến Tre', 'Cà Mau', 'Cần Thơ', 'Đồng Tháp', 'Hậu Giang', 'Kiên Giang', 'Long An', 'Sóc Trăng', 'Tiền Giang', 'Trà Vinh', 'Vĩnh Long', 'Long Xuyên', 'Châu Đốc', 'Tân Châu', 'Chợ Mới', 'Tri Tôn', 'An Phú', 'Phú Tân', 'Bạc Liêu', 'Hồng Dân', 'Giá Rai', 'Đông Hải', 'Phước Long', 'Bến Tre', 'Ba Tri', 'Châu Thành', 'Giồng Trôm', 'Bình Đại', 'Thạnh Phú', 'Cà Mau', 'Đầm Dơi', 'Ngọc Hiển', 'Năm Căn', 'Phú Tân', 'U Minh', 'Cao Lãnh', 'Sa Đéc', 'Lai Vung', 'Tam Nông', 'Tháp Mười', 'Tân Hồng', 'Hồng Ngự', 'Vị Thanh', 'Ngã Bảy', 'Phụng Hiệp', 'Châu Thành A', 'Rạch Giá', 'Phú Quốc', 'Hà Tiên', 'An Biên', 'An Minh', 'Giồng Riềng', 'Gò Quao', 'Kiên Lương', 'Tân Hiệp', 'U Minh Thượng', 'Tân An', 'Kiến Tường', 'Tân Thạnh', 'Tân Trụ', 'Thạnh Hóa', 'Vĩnh Hưng', 'Bến Lức', 'Cần Đước', 'Cần Giuộc', 'Đức Hòa', 'Mộc Hóa', 'Tân Hưng', 'Tân Thành', 'Thủ Thừa', 'Sóc Trăng', 'Kế Sách', 'Mỹ Tú', 'Vĩnh Châu', 'Cù Lao Dung', 'Long Phú', 'Mỹ Xuyên', 'Thạnh Trị', 'Trần Đề', 'Mỹ Tho', 'Cai Lậy', 'Châu Thành', 'Tân Phú Đông', 'Cái Bè', 'Cai Lậy', 'Chợ Gạo', 'Gò Công', 'Tân Phước', 'Tân Phú', 'Màng Thít', 'Trà Vinh', 'Càng Long', 'Cầu Kè', 'Tiểu Cần', 'Cầu Ngang', 'Châu Thành', 'Duyên Hải', 'Vĩnh Long', 'Bình Minh', 'Long Hồ', 'Tam Bình', 'Trà Ôn', 'Vũng Liêm', 'Mang Thít', 'Cần Thơ', 'Phong Điền', 'Thốt Nốt', 'Vĩnh Thạnh', 'Cờ Đỏ'];
const mientay_chuanhoa = mientay.map(item => item.toLowerCase());

const BXe_json = fs.readFileSync('./stations.json', 'utf8');
const BXe = JSON.parse(BXe_json);

const HcmDeparture = () => {
    axios.get('https://api.futabus.vn/booking/api/schedule/queryscheduleroute?Status=1', {
        headers: {
            "authorization": '3hqaY7KZy315L01L6KWi8ZtrVHuoFf7d',
            "user-agent": 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.54',
            "authority": 'api.futabus.vn',
            "clientapp": 'webApp'
        }
    })
    .then(async (res) => {
        const data = res.data.Data;

        const hcmTo_data = data.filter((item) => item.OriginCode === "TPHCM");

        const hcmTo_mientay = hcmTo_data.filter((item) => mientay_chuanhoa.includes(item.DestName.toLowerCase()));

        // const hcmTo_mientay_chuanhoa = hcmTo_mientay.map((item) => item.DestName.toLowerCase());

        // const hcmTo_mientay_chuanhoa_unique = [...new Set(hcmTo_mientay_chuanhoa)];

        // console.log(hcmTo_mientay_chuanhoa_unique);

        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate()+1);
        
        const [m, d, y] = tomorrow.toLocaleString({ timeZone: 'Asia/Ho_Chi_Minh' }).split(', ')[0].split('/');
        
        const day = d.length === 1 ? `0${d}` : d;
        const month = m.length === 1 ? `0${m}` : m;

        const getDate = `${day}/${month}/${y}`;

        const timer = ms => new Promise( res => setTimeout(res, ms));

        // console.log(getDate);

        const getTicket = async (hcmTo_mientay) => {
            const ticket = [];
            const trip = [];

            const chuanhoa = (dt) => {
                return slugify(dt, {
                    replacement: ' ',
                    lower: true,
                    locale: 'vi',
                    trim: true  
                });
            }

            let index = 0;
            for (const item of hcmTo_mientay) {

                trip.push({
                    departure_city: item.OriginName,
                    arrival_city: item.DestName,
                    bus_type: item.Kind,
                    duration: item.Duration,
                    distance: item.Distance,
                });

                if (index >= 0) {

                    const fetch_url = `https://futabus.vn/lich-trinh/ve-xe-phuong-trang-tp-ho-chi-minh-di-${slugify(item.DestName.toLowerCase(), {
                        replacement: '-',
                        lower: true,
                        locale: 'vi',
                        trim: true  
                    })}-${item.Id}.html?bookFromDate=${getDate}`;
        
                    try {
                        const res = await axios.get(fetch_url);
                    
                        const $ = cheerio.load(res.data);
                        $('.time-table > tbody > tr').each(function (idx) {
                            if (idx > 0 && $(this).find('td')[0].children[0].data.toLowerCase() !== 'giờ chạy') {

                                const b_xe = $('.time-detail-container > .header').contents()
                                .filter(function () {
                                    return this.type === "text";
                                })
                                .text().replaceAll("\n", "").trim();

                                const b_xe_arr = BXe.find((el) => el.location.toLowerCase() === item.DestName.toLowerCase());
                                const station = (b_xe_arr.stations.length === 1) ? b_xe_arr.stations[0] : b_xe_arr.stations.find(el => chuanhoa(b_xe).indexOf(chuanhoa(el.name.replaceAll('BX', '').trim())) !== -1 || chuanhoa(el.name).indexOf(chuanhoa(b_xe.replaceAll('sai gon', '').replaceAll('tp hcm', '').trim())) !== -1);
                                
                                const data = {
                                    departure_time: $(this).find('td')[0].children[0].data,
                                    arrival_time: $(this).find('.time-depart').text().trim(),
                                    departure_city: item.OriginName,
                                    arrival_city: item.DestName,
                                    ticket_type: item.Type,
                                    price: +(($(this).find('td.prices > p.price')).contents()
                                    .filter(function () {
                                        return this.type === "text";
                                    })
                                    .text().replace(/\D+/g, "")).trim(),
                                    travel_time: item.Duration,
                                    distance: item.Distance,
                                    departure_depot: 'BX Miền Tây',
                                    arrival_depot: `${station.name}`,
                                    bus_type: item.Kind,
                                    reserved_seats: 0,
                                    total_seats: 34,
                                }
                                ticket.push(data);
                            }
                        });
                    }
                    catch (err) { 
                        console.log(err);
                    }
        
                    await timer(1000);
                }

                index++;
                console.log(`${index}/${hcmTo_mientay.length}`);
            }
            return [ticket, trip];
        }

        const [tickets, trips] = await getTicket(hcmTo_mientay);

        fs.writeFile('./tickets_departure.json', JSON.stringify(tickets), 'utf8', (err)=>{
            if(err){
                console.log(err)
            }
        });

        fs.writeFile('./trips_departure.json', JSON.stringify(trips), 'utf8', (err)=>{
            if(err){
                console.log(err)
            }
        });
        
        // console.log(hcmTo_mientay);
    })
    .catch((err) => console.log(err));
}

const HcmArrival = () => {
    axios.get('https://api.futabus.vn/booking/api/schedule/queryscheduleroute?Status=1', {
        headers: {
            "authorization": '3hqaY7KZy315L01L6KWi8ZtrVHuoFf7d',
            "user-agent": 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.54',
            "authority": 'api.futabus.vn',
            "clientapp": 'webApp'
        }
    })
    .then(async (res) => {
        const data = res.data.Data;

        const hcmTo_data = data.filter((item) => item.DestCode === "TPHCM");

        const hcmTo_mientay = hcmTo_data.filter((item) => mientay_chuanhoa.includes(item.OriginName.toLowerCase()));

        // const hcmTo_mientay_chuanhoa = hcmTo_mientay.map((item) => item.DestName.toLowerCase());

        // const hcmTo_mientay_chuanhoa_unique = [...new Set(hcmTo_mientay_chuanhoa)];

        // console.log(hcmTo_mientay_chuanhoa_unique);

        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate()+1);
        
        const [m, d, y] = tomorrow.toLocaleString({ timeZone: 'Asia/Ho_Chi_Minh' }).split(', ')[0].split('/');
        
        const day = d.length === 1 ? `0${d}` : d;
        const month = m.length === 1 ? `0${m}` : m;

        const getDate = `${day}/${month}/${y}`;

        const timer = ms => new Promise( res => setTimeout(res, ms));

        // console.log(getDate);

        const getTicket = async (hcmTo_mientay) => {
            const ticket = [];
            const trip = [];

            let index = 0;
            for (const item of hcmTo_mientay) {

                trip.push({
                    departure_city: item.OriginName,
                    arrival_city: item.DestName,
                    bus_type: item.Kind,
                    duration: item.Duration,
                    distance: item.Distance,
                });

                if (index >= 0) {

                    const fetch_url = `https://futabus.vn/lich-trinh/ve-xe-phuong-trang-${slugify(item.OriginName.toLowerCase(), {
                        replacement: '-',
                        lower: true,
                        locale: 'vi',
                        trim: true  
                    })}-di-tp-ho-chi-minh-${item.Id}.html?bookFromDate=${getDate}`;
        
                    try {
                        const res = await axios.get(fetch_url);
                    
                        const $ = cheerio.load(res.data);
                        $('.time-table > tbody > tr').each(function (idx) {
                            if (idx > 0 && $(this).find('td')[0].children[0].data.toLowerCase() !== 'giờ chạy') {

                                const data = {
                                    departure_time: $(this).find('td')[0].children[0].data,
                                    arrival_time: $(this).find('.time-depart').text().trim(),
                                    departure_city: item.OriginName,
                                    arrival_city: item.DestName,
                                    ticket_type: item.Type,
                                    price: +(($(this).find('td.prices > p.price')).contents()
                                    .filter(function () {
                                        return this.type === "text";
                                    })
                                    .text().replace(/\D+/g, "")).trim(),
                                    travel_time: item.Duration,
                                    distance: item.Distance,
                                    departure_depot: `BX ${$('.startPoint > .name').text().trim().replaceAll('BX ', '')}`,
                                    arrival_depot: 'BX Miền Tây',
                                    bus_type: item.Kind,
                                    reserved_seats: 0,
                                    total_seats: 34,
                                }
                                ticket.push(data);
                            }
                        });
                    }
                    catch (err) { 
                        console.log(err);
                    }
        
                    await timer(1000);
                }

                index++;
                console.log(`${index}/${hcmTo_mientay.length}`);
            }
            return [ticket, trip];
        }

        const [tickets, trips] = await getTicket(hcmTo_mientay);

        fs.writeFile('./tickets_arrival.json', JSON.stringify(tickets), 'utf8', (err)=>{
            if(err){
                console.log(err)
            }
        });

        fs.writeFile('./trips_arrival.json', JSON.stringify(trips), 'utf8', (err)=>{
            if(err){
                console.log(err)
            }
        });
        
        // console.log(hcmTo_mientay);
    })
    .catch((err) => console.log(err));
}

const BxMienTay = () => {

    axios.get('https://api.futabus.vn/booking/api/schedule/queryscheduleroute?Status=1', {
        headers: {
            "authorization": '3hqaY7KZy315L01L6KWi8ZtrVHuoFf7d',
            "user-agent": 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.54',
            "authority": 'api.futabus.vn',
            "clientapp": 'webApp'
        }
    })
    .then(async (res) => {
        const data = res.data.Data;

        const hcmTo_data = data.filter((item) => item.DestCode === "TPHCM");

        const hcmTo_mientay = hcmTo_data.filter((item) => mientay_chuanhoa.includes(item.OriginName.toLowerCase()));

        // const hcmTo_mientay_chuanhoa = hcmTo_mientay.map((item) => item.DestName.toLowerCase());

        // const hcmTo_mientay_chuanhoa_unique = [...new Set(hcmTo_mientay_chuanhoa)];

        // console.log(hcmTo_mientay_chuanhoa_unique);

        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate()+1);

        const [m, d, y] = tomorrow.toLocaleString({ timeZone: 'Asia/Ho_Chi_Minh' }).split(', ')[0].split('/');
        
        const day = d.length === 1 ? `0${d}` : d;
        const month = m.length === 1 ? `0${m}` : m;

        const getDate = `${day}/${month}/${y}`;

        const timer = ms => new Promise( res => setTimeout(res, ms));

        // console.log(getDate);

        const getBX = async (hcmTo_mientay) => {
            const BX = [];

            let index = 0;
            for (const item of hcmTo_mientay) {

                if (index >= 0) {

                    const fetch_url = `https://futabus.vn/lich-trinh/ve-xe-phuong-trang-${slugify(item.OriginName.toLowerCase(), {
                        replacement: '-',
                        lower: true,
                        locale: 'vi',
                        trim: true  
                    })}-di-tp-ho-chi-minh-${item.Id}.html?bookFromDate=${getDate}`;
        
                    try {
                        const res = await axios.get(fetch_url);
                    
                        const $ = cheerio.load(res.data);
                        const bx = {
                            name: `BX ${$('.startPoint > .name').text().trim().replaceAll('BX ', '')}`,
                            address: $('.startPoint > .address').text().trim(),
                            phone: $('.startPoint > .phone').text().replace(' ', ''),
                        }

                        const pos = BX.findIndex(el => el.location.toLowerCase() === item.OriginName.toLowerCase());
                        
                        if (pos > -1) {
                            const alreadyHaveStation = BX[pos].stations.find(el => el.phone.replace(/\D+/g, '') === bx.phone.replace(/\D+/g, ''))
                            if (!alreadyHaveStation)
                                BX[pos].stations.push(bx);
                        }
                        else {
                            BX.push({
                                location: item.OriginName,
                                stations: [bx]
                            });
                        }
                    }
                    catch (err) { 
                        console.log(err);
                    }
        
                    await timer(1000);
                }

                index++;
            }
            return BX;
        }

        const BXS = await getBX(hcmTo_mientay);

        var saveJson = JSON.stringify(BXS);

        fs.writeFile('./stations.json', saveJson, 'utf8', (err)=>{
            if(err){
                console.log(err)
            }
        });
        
        // console.log(hcmTo_mientay);
    })
    .catch((err) => console.log(err));
}

// BxMienTay();

// HcmDeparture();

// HcmArrival();

// const ticket_hcm = fs.readFileSync('./tickets_departure.json', 'utf8');
// const ticket_mientay = fs.readFileSync('./tickets_arrival.json', 'utf8');

// const tickets = [...JSON.parse(ticket_hcm), ...JSON.parse(ticket_mientay)];

// fs.writeFileSync('./tickets.json', JSON.stringify(tickets), 'utf8');

// const trip_hcm = fs.readFileSync('./trips_departure.json', 'utf8');
// const trip_mientay = fs.readFileSync('./trips_arrival.json', 'utf8');

// const trips = [...JSON.parse(trip_hcm), ...JSON.parse(trip_mientay)];

// fs.writeFileSync('./trips.json', JSON.stringify(trips), 'utf8');