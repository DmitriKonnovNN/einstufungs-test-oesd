
import './App.css';
import {useEffect,useState} from "react";
import {fetchTaskSet} from "./client";
import Layout, {Content, Footer, Header} from "antd/es/layout/layout";
import {Button, Steps, message, Divider, Card, Col} from 'antd';
import {StarOutlined} from '@ant-design/icons';

const gridStyle = {
    width: '16%',
    textAlign: 'center',
    padding:'15px'
};

const {Step} = Steps;

const steps = [
    {
        title: "Gestartet!",
        taskContent: "auth-form",
        items:"items",
        description: "time left: 13 min",
        icon:  <StarOutlined />
    },
    {
        title: 'A1',
        taskContent: 'First-content',
        items:"items",
        description: "beginner"
    },
    {
        title: 'A2',
        taskContent: "Maximilian\n" +
            "Servus! Mein Name ist Maximilian Gruber. Ich komme aus Österreich und wohne in Wien. Ich habe eine Ausbildung zum Mechatroniker gemacht und arbeite jetzt in einer Autowerkstatt. Ich mag Autos! In der Freizeit spiele ich Fußball, fahre Rad und höre Musik. Ein Tag ohne Musik ist kein guter Tag!\n" +
            "\n" +
            "Anna\n" +
            "Grüß Gott! Ich heiße Anna Moser und komme aus Österreich, aus Klagenfurt. Jetzt wohne ich in Salzburg. Ich bin Musikerin und Lehrerin. Ich unterrichte am Mozarteum. Ich mag meine Studenten, die Arbeit macht mir Spaß. Meine Hobbys sind Reisen und Lesen. Ich lese gern Romane.\n" +
            "\n" +
            "Katharina\n" +
            "Guten Tag! Ich heiße Katharina Berger. Ich wohne in Linz, aber ich komme aus Graz. Ich bin Krankenschwester von Beruf und arbeite im Spital. Meine Hobbys sind kochen und Filme sehen. Ich mag auch Sport: Schwimmen tut gut! Ich schwimme zweimal pro Woche.\n",
        items:"items",
        description: "pre-intermediate"
    },
    {
        title: 'B1',
        taskContent: 'Last-content',
        items:"items",
        description:"intermediate",
    },    {
        title: 'B2',
        taskContent: 'Last-content',
        items:"items",
        description:"upper-intermediate",
    },
    {
        title: 'C1',
        taskContent: 'Last-content',
        items:"items",
        description:"advanced",
    },
    {
        title: 'C2',
        taskContent: 'Last-content',
        items:"items",
        description:"mastery",
    },
    {
        title: 'Ergebnisse',
        taskContent: 'Last-content',
        items:"items",
        description:"result",
    },
];

function App() {

    //const [taskSet, setTaskSet] = useState([])
    const [current, setCurrent] = useState(0)
    const [precentDone, setPrecentDone] = useState(0)
    const next = () => {
        setCurrent(current + 1);
        setPrecentDone(precentDone + 100/7)
    };
    const prev = () => {
        setCurrent(current - 1);
        setPrecentDone(precentDone - 100/7)
    };

    useEffect(()=>{
        console.log(`component mounted!`)
        fetchTaskSet()},[]);

  return (<Layout className = "site-layout" style={{ minHeight: '100vh' }}>
            <Header className="ant-layout-header" style={{ padding: 0 }}>
            </Header>
            <Content >
                <div className = "split-left-content">
                        <Steps current={current} percent={precentDone} direction={"vertical"}>
                        {steps.map((item) => (
                            <Step key={item.title} title={item.title} description={item.description} icon={item.icon} />
                        ))}
                    </Steps>
                </div>
                <div className= "split-center-content">
                    <div className="steps-content">
{/*                        <Card title="Kreuzen Sie an. Was passt zu Maximilian, Anna, Katharina?" size="small" >
                            <p> {steps[current].taskContent}</p>
                        </Card>*/}
                        <Divider orientation="left">Lesen Sie den Text und beantworten Sie die Fragen</Divider>
                                <Col span={36}>
                                    <Card title="Maximilian"  bordered={true} size={"small"}>
                                        Servus! Mein Name ist Maximilian Gruber. Ich komme aus Österreich und wohne in Wien. Ich habe eine Ausbildung zum Mechatroniker gemacht und arbeite jetzt in einer Autowerkstatt. Ich mag Autos! In der Freizeit spiele ich Fußball, fahre Rad und höre Musik. Ein Tag ohne Musik ist kein guter Tag!
                                    </Card>
                                </Col>
                                <Col span={36}>
                                    <Card title="Anna" bordered={true} size={"small"}>
                                        Grüß Gott! Ich heiße Anna Moser und komme aus Österreich, aus Klagenfurt. Jetzt wohne ich in Salzburg. Ich bin Musikerin und Lehrerin. Ich unterrichte am Mozarteum. Ich mag meine Studenten, die Arbeit macht mir Spaß. Meine Hobbys sind Reisen und Lesen. Ich lese gern Romane.
                                    </Card>
                                </Col>
                                <Col span={36}>
                                    <Card title="Katharina"bordered={true} size={"small"}>
                                        Guten Tag! Ich heiße Katharina Berger. Ich wohne in Linz, aber ich komme aus Graz. Ich bin Krankenschwester von Beruf und arbeite im Spital. Meine Hobbys sind kochen und Filme sehen. Ich mag auch Sport: Schwimmen tut gut! Ich schwimme zweimal pro Woche.
                                    </Card>
                                </Col>
                    </div>
                    <div className="steps-content">
                        <Card title="Kreuzen Sie an. Was passt zu Maximilian, Anna, Katharina?">
                            <Card.Grid hoverable={false} style={gridStyle}>.</Card.Grid>
                            <Card.Grid hoverable={false} style={gridStyle}>… wohnt in Salzburg</Card.Grid>
                            <Card.Grid hoverable={false} style={gridStyle}>… kommt aus Graz</Card.Grid>
                            <Card.Grid hoverable={false} style={gridStyle}>… mag Musik</Card.Grid>
                            <Card.Grid hoverable={false} style={gridStyle}>… arbeitet als Lehrerin</Card.Grid>
                            <Card.Grid hoverable={false} style={gridStyle}>… kocht und schwimmt gern</Card.Grid>
                            <Card.Grid hoverable={false} style={gridStyle}>Anna</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid style={gridStyle}>x</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid style={gridStyle}>x</Card.Grid>
                            <Card.Grid hoverable={false}style={gridStyle}>Maximilian</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid style={gridStyle}>x</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid hoverable={false}style={gridStyle}>Katharina</Card.Grid>
                            <Card.Grid style={gridStyle}>x</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                            <Card.Grid style={gridStyle}>x</Card.Grid>
                            <Card.Grid style={gridStyle}>?</Card.Grid>
                        </Card>
                    </div>
                </div>
                <div className="split-right-content">
                    <div className="steps-action">
                        {current === steps.length - 1 && (
                            <Button type="primary" onClick={() => message.success('Processing complete!')}>
                                Einreichen!
                            </Button>
                        )}
                        {current > 0 && (
                            <Button
                                style={{
                                    margin: '0 8px',
                                }}
                                onClick={() => prev()}
                            >
                                rückwärts
                            </Button>
                        )}
                        {current < steps.length - 1 && (
                            <Button type="primary" onClick={() => next()}>
                                vorwärts
                            </Button>
                        )}
                    </div>
                </div>
            </Content>
            <Footer >

            </Footer>
        </Layout>
  );
}

export default App;
