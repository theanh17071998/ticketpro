import * as React from 'react';
import { FormattedMessage } from 'react-intl';
import { Row, Col } from '../../../common/components/elements';
import LoadingButton from '../../../common/components/LoadingButton';
import Card from '../components/Card';
import Carousel from '../components/Carousel';
import axios from 'axios';

interface Props { }

const tutorialSteps = [
  {
    label: 'San Francisco – Oakland Bay Bridge, United States',
    imgPath: 'https://picsum.photos/692/266',
  },
  {
    label: 'Bird',
    imgPath: 'https://picsum.photos/691/266',
  },
  {
    label: 'Bali, Indonesia',
    imgPath: 'https://picsum.photos/691/265',
  },
  {
    label: 'NeONBRAND Digital Marketing, Las Vegas, United States',
    imgPath: 'https://picsum.photos/693/266',
  },
  {
    label: 'Goč, Serbia',
    imgPath: 'https://picsum.photos/691/268',
  },
];

// const listTicket = [
//   {
//     id: 1,
//     img: 'https://picsum.photos/250/120',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '10/10/2020',
//     category: 'Nightlife',
//   },
//   {
//     id: 2,
//     img: 'https://picsum.photos/251/120',
//     title: 'Đêm nhạc FRANZ LISZT & ANTONIN DVOŘÁK',
//     time: '03/10/2020',
//     category: 'Art & Culture',
//   },
//   {
//     id: 3,
//     img: 'https://picsum.photos/250/121',
//     title: 'Kịch IDECAF: MƯU BÀ TÚ',
//     time: '03/10/2020',
//     category: 'Theater & Plays',
//   },
//   {
//     id: 4,
//     img: 'https://picsum.photos/250/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
//   {
//     id: 5,
//     img: 'https://picsum.photos/251/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
//   {
//     id: 6,
//     img: 'https://picsum.photos/251/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
//   {
//     id: 7,
//     img: 'https://picsum.photos/251/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
//   {
//     id: 8,
//     img: 'https://picsum.photos/251/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
//   {
//     id: 9,
//     img: 'https://picsum.photos/251/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
//   {
//     id: 10,
//     img: 'https://picsum.photos/251/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
//   {
//     id: 11,
//     img: 'https://picsum.photos/251/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
//   {
//     id: 12,
//     img: 'https://picsum.photos/251/122',
//     title: 'Workshop vẽ tranh màu nước: Classy',
//     time: '02/10/2020',
//     category: 'Courses',
//   },
// ];


const Home: React.FunctionComponent<Props> = () => {
  const listTicket = React.useState([]);

  // React.useEffect(() => {
  //   let response = await axios.get()
  // }, [listTicket])
  return (
    <Col>
      <div style={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
        <Carousel tutorialSteps={tutorialSteps} />
      </div>
      <div
        style={{
          display: 'flex',
          marginTop: '10px',
          flexWrap: 'wrap',
          justifyContent: 'center',
        }}
      >
        <Card listTicket={listTicket} />
      </div>
      <Row style={{ marginTop: '16px', justifyContent: 'center' }}>
        <LoadingButton
          style={{ minWidth: 160, marginRight: 32 }}
          size="large"
          type="submit"
          variant="contained"
          color="secondary"
          disableElevation
        >
          <FormattedMessage id="more" />
        </LoadingButton>
      </Row>
    </Col>
  );
};

export default Home;
